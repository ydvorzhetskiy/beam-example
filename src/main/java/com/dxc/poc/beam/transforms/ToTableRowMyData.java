package com.dxc.poc.beam.transforms;

import com.dxc.poc.beam.dto.MyData;
import com.google.api.services.bigquery.model.TableRow;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;


@Slf4j
public class ToTableRowMyData extends DoFn<MyData, TableRow> {

    private Counter goodRecordCounter = Metrics.counter("beam-example", "good_record_count");
    private Counter badRecordCounter = Metrics.counter("beam-example", "bad_record_count");


    @ProcessElement
    public void processElement(@Element MyData myData, OutputReceiver<TableRow> out) {
        try {
            val row = toTableRow(myData);
            out.output(row);
            goodRecordCounter.inc();
        } catch (Exception e) {
            badRecordCounter.inc();
            log.error("Unexpected exception:", e);
        }
    }

    @SneakyThrows
    public TableRow toTableRow(MyData myData) {
        val row = new TableRow();
        row.set("field_a", myData.getField_a());
        row.set("field_b", myData.getField_b());
        return row;
    }
}
