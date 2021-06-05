package com.dxc.poc.beam.validators;

import com.dxc.poc.beam.dto.Pnr;
import com.sabre.gcp.validation.NonNullValuesValidator;
import com.sabre.gcp.validation.Validator;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

@Validator(
    validationChainId = "pnrChain"
)
public class PnrTicketNumberNullValidator extends NonNullValuesValidator<Pnr> {

    @Override
    protected List<String> getNonNullFieldNames() {
        return singletonList("ticketNumber");
    }
}
