package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.Pnr;
import com.google.api.services.bigquery.model.TableRow;
import lombok.val;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;

public class ToTableRowDoFn extends DoFn<Pnr, TableRow> {

    private Counter counter = Metrics.counter(ToTableRowDoFn.class, "pnr-to-table-row-counter");

    @ProcessElement
    public void processElement(@Element Pnr pnr, OutputReceiver<TableRow> out) {
        val row = PnrConverter.toTableRow(pnr);
        out.output(row);
        counter.inc();
    }
}
