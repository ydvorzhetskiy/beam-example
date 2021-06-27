package com.dxc.poc.beam;

import com.dxc.poc.beam.pipeline.GsToBqOptions;
import com.dxc.poc.beam.pipeline.PnrGsToBqPipeline;
import com.sabre.gcp.logging.CloudLogger;
import lombok.val;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.KV;


public class GsToBqApplication {

    private static final CloudLogger LOG = CloudLogger.getLogger(GsToBqApplication.class,
        KV.of("application-name", "beam-example"),
        KV.of("job-name", "dxc-stream-demo-job"));

    public static void main(String[] args) {
        LOG.info("Entering the Application", KV.of("1", "2"));

        val options = PipelineOptionsFactory.fromArgs(args)
            .withValidation()
            .as(GsToBqOptions.class);
        options.setRunner(DataflowRunner.class);

        if (options.getTemplateLocation() != null) {
            LOG.info("Call for a template generation");
            try {
                PipelineResult result = Pipeline.create(options).run();
                result.getState();
                result.waitUntilFinish();
            } catch (UnsupportedOperationException e) {
                // do nothing
                // This is workaround for https://issues.apache.org/jira/browse/BEAM-9337
            } catch (Exception e) {
                // Here is an example of error logging (Logback + Slf4j)
                LOG.error("Unexpected error", e);
            }
        } else {
            LOG.info("Start processing");
            PnrGsToBqPipeline.createAndRunPipeline(options);
        }

        LOG.info("GS2BQ processing finished.");
    }
}
