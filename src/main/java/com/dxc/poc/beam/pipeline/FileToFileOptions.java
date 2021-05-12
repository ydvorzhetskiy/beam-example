package com.dxc.poc.beam.pipeline;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.*;

public interface FileToFileOptions extends DataflowPipelineOptions {

    @Description("Path of the file to read from")
    @Validation.Required
    String getInputFile();

    void setInputFile(String value);

    @Description("Path of the file to write to")
    @Validation.Required
    String getOutputFile();

    void setOutputFile(String value);
}
