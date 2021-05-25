package com.dxc.poc.beam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Pnr implements Serializable {

    @JsonProperty("pr_locator_id")
    private String prLocatorId;

    @JsonProperty("ticket_number")
    private String ticketNumber;

    @JsonProperty("pr_create_date")
    private String prCreateDate;

    @JsonProperty("pr_sequence")
    private String prSequence;

    @JsonProperty("from_datetime")
    private String fromDatetime;

    @JsonProperty("tr_datetime")
    private String trDatetime;

    @JsonProperty("creditcard")
    private List<CreditCard> creditCards;
}
