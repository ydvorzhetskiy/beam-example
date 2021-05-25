package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.CreditCard;
import com.dxc.poc.beam.dto.Pnr;
import lombok.val;
import org.junit.Test;

import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class PnrConverterTest {

    @Test
    public void shouldConvertPnrToTableRow() {
        val row = PnrConverter.toTableRow(pnr());
        assertEquals("FGDNKU", row.get("pr_locator_id"));
        assertEquals("1c345a7812u45", row.get("ticket_number"));
        assertEquals("2021-05-11", row.get("pr_create_date"));
        assertEquals(1, row.get("pr_sequence"));
        assertEquals("2021-05-11 13:35:12", row.get("from_datetime"));
        assertEquals("2021-05-11 13:35:12", row.get("tr_datetime"));
        assertEquals("Visa", row.get("creditcard_network"));
        assertEquals("4485764219977839", row.get("creditcard_number"));
    }

    private Pnr pnr() {
        val pnr = new Pnr();
        pnr.setPrLocatorId("FGDNKU");
        pnr.setTicketNumber("1c345a7812u45");
        pnr.setPrCreateDate("2021-05-11");
        pnr.setPrSequence("100");
        pnr.setFromDatetime("2021-05-11 13:35:12");
        pnr.setTrDatetime("2021-05-11 13:35:12");
        val card = new CreditCard();
        card.setIssuingNetwork("Visa");
        card.setCardNumber("4485764219977839");
        pnr.setCreditCards(singletonList(card));
        return pnr;
    }
}