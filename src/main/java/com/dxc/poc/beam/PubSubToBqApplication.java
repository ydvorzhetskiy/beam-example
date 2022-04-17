package com.dxc.poc.beam;

import com.dxc.poc.beam.pipeline.PnrPubSubToBqPipeline;
import com.dxc.poc.beam.pipeline.PubSubToBqOptions;
import lombok.val;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PubSubToBqApplication {

    private static final Logger LOG = LoggerFactory.getLogger(PubSubToBqApplication.class);

    public static void main(String[] args) {
        LOG.info("Creating a pipeline...");
        val options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(PubSubToBqOptions.class);
        options.setRunner(DataflowRunner.class);
        PnrPubSubToBqPipeline.createAndRunPipeline(options);
    }
}
