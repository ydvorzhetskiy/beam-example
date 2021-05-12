package com.dxc.poc.beam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreditCard implements Serializable {

    @JsonProperty("issuing_network")
    private String issuingNetwork;

    @JsonProperty("card_number")
    private String cardNumber;
}
