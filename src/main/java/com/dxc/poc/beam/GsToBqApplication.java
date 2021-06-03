package com.dxc.poc.beam;

import com.dxc.poc.beam.pipeline.GsToBqOptions;
import com.dxc.poc.beam.pipeline.PnrGsToBqPipeline;
import com.dxc.poc.beam.utils.logging.LogContext;
import com.sabre.gcp.logging.CloudLogger;
import lombok.val;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GsToBqApplication {

    // Here is an example of Logback and Slf4j usage
    private static final Logger log = LoggerFactory.getLogger(GsToBqApplication.class);
    // Here is an example of wrapper usage
    private static final CloudLogger cloudLogger = CloudLogger.getLogger(GsToBqApplication.class,
                                                                KV.of("application-name", "beam-example"),
                                                                KV.of("job-name", "dxc-stream-demo-job"));

    public static void main(String[] args) {

        // Here is an example of info logging in main thread (Wrapper)
        cloudLogger.info("Entering the Application");

        // Here is an example of manual adding JSON parameters to the context (using Slf4j and Logback)
        LogContext.setLabel("custom_label", "custom_value");
        LogContext.setJsonVar("custom_json_var", "custom_json_value");
        LogContext.setJsonVar("message", "This is a message injected to JSON");
        log.info("Starting the GS2BQ Example pipeline 1...");
        LogContext.clearJsonVar();

        // another message
        LogContext.setLabel("custom_label", "custom_value_new");
        LogContext.setLabel("custom_label_2", "custom_value_new_2");

        // Adding custom labels using enhancer (Logback and Slf4j)
        log.info("label1=value1|Starting the GS2BQ Example pipeline 2...");
        LogContext.clearJsonVar();
        log.error("label2=value2|label2=value2|Custom Labels example");

        val options = PipelineOptionsFactory.fromArgs(args)
            .withValidation()
            .as(GsToBqOptions.class);
        options.setRunner(DataflowRunner.class);

        if (options.getTemplateLocation() != null) {
            log.info("Call for a template generation");
            try {
                PipelineResult result = Pipeline.create(options).run();
                result.getState();
                result.waitUntilFinish();
            } catch (UnsupportedOperationException e) {
                // do nothing
                // This is workaround for https://issues.apache.org/jira/browse/BEAM-9337
            } catch (Exception e) {
                // Here is an example of error logging (Logback + Slf4j)
                log.error("Unexpected error", e);
            }
        } else {
            log.info("Start processing");
            PnrGsToBqPipeline.createAndRunPipeline(options);
        }

        log.info("GS2BQ processing finished.");
    }
}
