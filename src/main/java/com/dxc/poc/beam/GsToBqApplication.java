package com.dxc.poc.beam;

import com.dxc.poc.beam.pipeline.GsToBqOptions;
import com.dxc.poc.beam.pipeline.PnrGsToBqPipeline;
import lombok.val;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
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

        if(options.getTemplateLocation()!=null){
            logger.info("Call for a template generation");
            PipelineResult result = Pipeline.create(options).run();
            try {
                result.getState();
                result.waitUntilFinish();
                //System.exit(0);
            } catch (UnsupportedOperationException e) {
                // do nothing
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("Start processing");
            PnrGsToBqPipeline.createAndRunPipeline(options);
        }


        //PnrGsToBqPipeline.createAndRunPipeline(options);

        logger.info("GS2BQ processing finished.");
    }
}
