package com.dxc.poc.beam.pipeline;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.options.Validation;

public interface PubSubToBqOptions extends GcpOptions, StreamingOptions, DataflowPipelineOptions {

    @Description("The Cloud Pub/Sub topic to read from.")
    @Validation.Required
    String getInputTopic();

    void setInputTopic(String value);

    @Description("Output dataset ID")
    @Validation.Required
    String getDataset();

    void setDataset(String value);

    @Description("Output table name")
    @Default.String("pnr_output")
    String getTableName();

    void setTableName(String value);
}
