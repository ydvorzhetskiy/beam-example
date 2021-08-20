package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.Pnr;
import com.google.api.services.bigquery.model.TableRow;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.beam.sdk.transforms.DoFn;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class PnrConverter {

    @SneakyThrows
    public static TableRow toTableRow(Pnr pnr) {

        val bq = BigQueryOptions.getDefaultInstance().getService();
        val jobConfiguration = QueryJobConfiguration
            .newBuilder("SELECT code, value" +
                " FROM `beam_example.mdm_table`")
            .setUseQueryCache(true)
            .build();

        val start = Instant.now();
        Iterable<FieldValueList> values = bq.query(jobConfiguration).getValues();
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
