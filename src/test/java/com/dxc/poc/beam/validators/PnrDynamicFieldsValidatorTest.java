package com.dxc.poc.beam.validators;

import com.dxc.poc.beam.dto.Pnr;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class PnrDynamicFieldsValidatorTest {

    @Test
    public void testValidNonNull() {
        val obj = new Pnr();
        obj.setPrLocatorId("LOCATORID");
        obj.setPrSequence("123");

        val validator = new PnrDynamicFieldsValidator();
        assertTrue(validator.doValidation(obj).isEmpty());
    }

    @Test
    public void testInValidNonNull() {
        val obj = new Pnr();
        obj.setPrLocatorId("LOCATORID");

        val validator = new PnrDynamicFieldsValidator();
        assertFalse(validator.doValidation(obj).isEmpty());
    }
}
