package com.dxc.poc.beam.validators;

import com.dxc.poc.beam.dto.Pnr;
import com.sabre.gcp.validation.NonNullValuesValidator;
import com.sabre.gcp.validation.Validator;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Validator(
    validationChainId = "pnrChain",
    validateAfter = PnrDynamicFieldsValidator.class
)
public class PnrDynamicFieldsValidator extends NonNullValuesValidator<Pnr> {

    private final List<String> fieldNames;

    public PnrDynamicFieldsValidator() {
        try {
            Properties prop = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("app.properties");
            prop.load(inputStream);
            String fieldNamesAsString = prop.getProperty("pnr.validation.non-null-fields");
            this.fieldNames = Arrays.asList(fieldNamesAsString.split(","));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected List<String> getNonNullFieldNames() {
        return fieldNames;
    }
}
