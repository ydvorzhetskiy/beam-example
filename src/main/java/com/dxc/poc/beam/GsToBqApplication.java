package com.dxc.poc.beam;

import com.dxc.poc.beam.pipeline.GsToBqOptions;
import com.dxc.poc.beam.pipeline.PnrGsToBqPipeline;
import lombok.val;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GsToBqApplication {

    private static final Logger logger = LoggerFactory.getLogger(GsToBqApplication.class);

    public static void main(String[] args) {

        logger.info("Starting the GS2BQ Example pipeline1...");

        val options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(GsToBqOptions.class);
        options.setRunner(DataflowRunner.class);
        PnrGsToBqPipeline.createAndRunPipeline(options);

        logger.info("GS2BQ processing finished.");
    }
}
