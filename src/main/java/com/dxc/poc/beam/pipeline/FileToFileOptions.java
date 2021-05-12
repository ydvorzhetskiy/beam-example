package com.dxc.poc.beam.pipeline;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface FileToFileOptions extends PipelineOptions {

    @Description("Path of the file to read from")
    @Validation.Required
    String getInputFile();

    void setInputFile(String value);

    @Description("Path of the file to write to")
    @Validation.Required
    String getOutputFile();

    void setOutputFile(String value);
}
