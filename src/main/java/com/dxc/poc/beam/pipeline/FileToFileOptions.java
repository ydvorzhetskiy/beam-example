package com.dxc.poc.beam.pipeline;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.*;

public interface FileToFileOptions extends DataflowPipelineOptions {

    @Description("Path of the file to read from")
    @Validation.Required
    String getInputFile();

    void setInputFile(String value);

    @Description("Output dataset ID")
    @Validation.Required
    String getDataset();

    void setDataset(String value);

    @Description("Output table name")
    @Default.String("pnr_output")
    String getTableName();

    void setTableName(String value);
}
