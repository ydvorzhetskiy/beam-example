package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.MyData;
import com.dxc.poc.beam.options.BqToBqOptions;
import com.dxc.poc.beam.transforms.*;
import lombok.val;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.joda.time.Duration;


public class PnrBqToBqPipeline {

    public static void createAndRunPipeline(BqToBqOptions options) {
        val tableRefOut = BqMetadataFactory.createTableReference(
                options.getProject(), options.getDataset(), options.getTableName());
        val tableRefIn = BqMetadataFactory.createTableReference(
                options.getProject(), options.getDataset(), options.getTableNameInput());
        val schema = BqMetadataFactory.createTableSchemeMyData();

        Pipeline.create(options)
                .apply("Read from BigQuery query", BigQueryIO.readTableRows().from(tableRefIn))
                .apply("TableRows to MyData", MapElements.into(TypeDescriptor.of(MyData.class)).via(MyData::fromTableRow))
                .setCoder(SerializableCoder.of(MyData.class))
                .apply(ParDo.of(new AddTimestamp()))
                .apply(Window.into(FixedWindows.of(Duration.standardSeconds(10))))
                .apply(ParDo.of(new FakeKvPair()))
                .apply("Stateful doFn", ParDo.of(new StatefulDoFn()))
                .apply("Convert to table row", ParDo.of(new ToTableRowMyData()))
                .apply("Write to BQ", BigQueryIO.writeTableRows()
                        .to(tableRefOut)
                        .withSchema(schema)
                        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                        .withCustomGcsTempLocation(ValueProvider.StaticValueProvider.of(options.getGcpTempLocation())))
                .getPipeline()
                .run().waitUntilFinish();
    }
}
