package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.Pnr;
import lombok.val;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.extensions.jackson.ParseJsons;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.ParDo;

public class PnrGsToBqPipeline {

    public static void createAndRunPipeline(GsToBqOptions options) {
        val tableRef = BqMetadataFactory.createTableReference(
                options.getProject(), options.getDataset(), options.getTableName());
        val schema = BqMetadataFactory.createTableSchema();

        Pipeline.create(options)
                .apply("Read JSON from file",
                        TextIO.read().from(options.getInputFile()))
                .apply("Parse JSON to DTO",
                        ParseJsons.of(Pnr.class))
                .setCoder(SerializableCoder.of(Pnr.class))
                .apply("Convert to table row",
                        ParDo.of(new ToTableRowDoFn()))
                .apply("Write to BQ", BigQueryIO.writeTableRows()
                        .to(tableRef)
                        .withSchema(schema)
                        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                        .withCustomGcsTempLocation(ValueProvider.StaticValueProvider.of(options.getGcpTempLocation())))
                .getPipeline()
                .run().waitUntilFinish();
    }
}