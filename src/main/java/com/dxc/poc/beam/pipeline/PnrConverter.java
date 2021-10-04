package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.Pnr;
import com.google.api.services.bigquery.model.TableRow;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PnrConverter {
    private final static Logger LOG = LoggerFactory.getLogger(PnrConverter.class);

    public static TableRow toTableRow(Pnr pnr) {
        LOG.info("Transforming...");
        val row = new TableRow();
        row.set("pr_locator_id", pnr.getPrLocatorId());
        row.set("ticket_number", pnr.getTicketNumber());
        row.set("pr_create_date", pnr.getPrCreateDate());
        row.set("pr_sequence", pnr.getPrSequence());
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
