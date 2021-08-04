package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.Pnr;
import com.google.api.services.bigquery.model.TableRow;
import com.sabre.gcp.logging.CloudLogger;
import lombok.val;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

public class ToTableRowDoFn extends DoFn<Pnr, TableRow> {

    private final CloudLogger log = CloudLogger.getLogger(ToTableRowDoFn.class,
        KV.of("application-name", "beam-example"),
        KV.of("job-name", "dxc-stream-demo-job"));

    private final Counter goodRecordCounter = Metrics.counter("beam-example", "good_record_count");
    private final Counter badRecordCounter = Metrics.counter("beam-example", "bad_record_count");

    @ProcessElement
    public void processElement(@Element Pnr pnr, OutputReceiver<TableRow> out) {

        val row = PnrConverter.toTableRow(pnr);
        out.output(row);
        goodRecordCounter.inc();
        if (pnr.getTicketNumber().endsWith("1")) {
            log.error("Repeated error message! " + pnr.getTicketNumber());
            throw new RuntimeException("Repeated exception!" + pnr.getTicketNumber());
        }
    }
}
