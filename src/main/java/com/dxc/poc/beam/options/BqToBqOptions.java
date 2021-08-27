package com.dxc.poc.beam.options;

import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.options.Validation;

public interface BqToBqOptions extends GcpOptions, StreamingOptions {

    @Description("Output dataset ID")
    @Validation.Required
    String getDataset();

    void setDataset(String value);

    @Description("Output table name")
    @Default.String("pnr_output")
    String getTableName();

    void setTableName(String value);

    @Description("Input table name")
    @Default.String("test_stateful")
    String getTableNameInput();

    void setTableNameInput(String value);
}
