package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.Pnr;
import com.google.api.services.bigquery.model.TableRow;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;


@Slf4j
public class ToTableRowDoFn extends DoFn<Pnr, TableRow> {

    private Counter goodRecordCounter = Metrics.counter("beam-example", "good_record_count");
    private Counter badRecordCounter = Metrics.counter("beam-example", "bad_record_count");

    @ProcessElement
    public void processElement(@Element Pnr pnr, OutputReceiver<TableRow> out) {

        try {
            val row = PnrConverter.toTableRow(pnr);
            out.output(row);
            goodRecordCounter.inc();
        } catch (NumberFormatException e){
            badRecordCounter.inc();
            log.error("beam-example=Numeric_Validation_Error|Number format validation");
        } catch (Exception e) {
            log.error("Unexpected exception:", e);
        }
    }
}
