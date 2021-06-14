package com.dxc.poc.beam.validators;

import com.dxc.poc.beam.dto.Pnr;
import com.sabre.gcp.validation.BaseValidator;
import com.sabre.gcp.validation.NonNullValuesValidator;
import com.sabre.gcp.validation.Validator;
import lombok.val;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Validator(
    validationChainId = "pnrChain",
    validateAfter = NonNullValuesValidator.class
)
public class TicketNumberLengthValidator implements BaseValidator<Pnr> {

    @Override
    public List<String> doValidation(Pnr pnr) {
        if (pnr.getTicketNumber().length() > 20) {
            return Collections.singletonList(
                "Ticket number '" + pnr.getTicketNumber() + " is invalid"
            ); // invalid
        }
        return Collections.emptyList(); // valid
    }
}
