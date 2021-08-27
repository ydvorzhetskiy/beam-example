package com.dxc.poc.beam.transforms;

import com.dxc.poc.beam.dto.Pnr;
import com.google.api.services.bigquery.model.TableRow;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;

import java.time.Duration;
import java.time.Instant;


@Slf4j
public class ToTableRowDoFn extends DoFn<Pnr, TableRow> {

    private Counter goodRecordCounter = Metrics.counter("beam-example", "good_record_count");
    private Counter badRecordCounter = Metrics.counter("beam-example", "bad_record_count");


    private TableResult result;

    @Setup
    public void init() throws InterruptedException {
        val bq = BigQueryOptions.getDefaultInstance().getService();
        val jobConfiguration = QueryJobConfiguration
            .newBuilder("SELECT code, value" +
                " FROM `beam_example.mdm_table`")
            .setUseQueryCache(true)
            .build();
        result = bq.query(jobConfiguration);
    }

    @ProcessElement
    public void processElement(@Element Pnr pnr, OutputReceiver<TableRow> out) {

        try {
            val row = toTableRow(pnr);
            out.output(row);
            goodRecordCounter.inc();
        } catch (NumberFormatException e){
            badRecordCounter.inc();
            log.error("beam-example=Numeric_Validation_Error|Number format validation");
        } catch (Exception e) {
            log.error("Unexpected exception:", e);
        }
    }

    @SneakyThrows
    public TableRow toTableRow(Pnr pnr) {

        val start = Instant.now();
        Iterable<FieldValueList> values = result.getValues();
        int numberOfRows = 0;
        for (FieldValueList value : values) {
            numberOfRows++;
        }
        val end = Instant.now();
        val queryTimeMs = Duration.between(start, end).toMillis();
        log.info("Query time: {} ms, Total rows: {}", queryTimeMs, numberOfRows);

        val row = new TableRow();
        row.set("pr_locator_id", pnr.getPrLocatorId());
        row.set("ticket_number", pnr.getTicketNumber());
        row.set("pr_create_date", pnr.getPrCreateDate());
        row.set("pr_sequence", Integer.parseInt(pnr.getPrSequence()));
        row.set("from_datetime", pnr.getFromDatetime());
        row.set("tr_datetime", pnr.getTrDatetime());
        if (!pnr.getCreditCards().isEmpty()) {
            val card = pnr.getCreditCards().get(0);
            row.set("creditcard_network", card.getIssuingNetwork());
            row.set("creditcard_number", card.getCardNumber());
        }
        return row;
    }
}
