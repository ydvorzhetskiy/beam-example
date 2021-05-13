package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.Pnr;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import lombok.val;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.extensions.jackson.ParseJsons;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

import java.util.ArrayList;
import java.util.List;

public class PnrGsToBqPipeline {

    static class ToTableRow extends DoFn<Pnr, TableRow> {
        @ProcessElement
        public void processElement(@Element Pnr pnr, OutputReceiver<TableRow> out) {
            val row = PnrConverter.toTableRow(pnr);
            out.output(row);
        }
    }

    public static void createAndRunPipeline(GsToBqOptions options) {
        val tableRef = new TableReference();
        tableRef.setProjectId(options.getProject());
        tableRef.setDatasetId(options.getDataset());
        tableRef.setTableId(options.getTableName());

        List<TableFieldSchema> fieldDefs = new ArrayList<>();
        fieldDefs.add(new TableFieldSchema().setName("pr_locator_id").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("ticket_number").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("pr_create_date").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("pr_sequence").setType("NUMERIC"));
        fieldDefs.add(new TableFieldSchema().setName("from_datetime").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("tr_datetime").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("creditcard_network").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("creditcard_number").setType("STRING"));

        Pipeline.create(options)
                .apply("Read JSON from file",
                        TextIO.read().from(options.getInputFile()))
                .apply("Parse JSON to DTO",
                        ParseJsons.of(Pnr.class))
                .setCoder(SerializableCoder.of(Pnr.class))
                .apply("Convert to table row",
                        ParDo.of(new ToTableRow()))
                .apply("Write to BQ", BigQueryIO.writeTableRows()
                        .to(tableRef)
                        .withSchema(new TableSchema().setFields(fieldDefs))
                        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                        .withCustomGcsTempLocation(ValueProvider.StaticValueProvider.of(options.getGcpTempLocation())))
                .getPipeline()
                .run().waitUntilFinish();
    }
}
