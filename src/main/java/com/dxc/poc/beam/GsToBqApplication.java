package com.dxc.poc.beam;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import com.dxc.poc.beam.pipeline.GsToBqOptions;
import com.dxc.poc.beam.pipeline.PnrGsToBqPipeline;
import com.dxc.poc.beam.utils.logging.CloudLogger;
import com.dxc.poc.beam.utils.logging.LogContext;
import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class GsToBqApplication {

    public static final Logger log = LoggerFactory.getLogger(GsToBqApplication.class);
    private static final CloudLogger cloudLogger = CloudLogger.getLogger(GsToBqApplication.class);

    static Logging logging = LoggingOptions.getDefaultInstance().getService();

    static void logSmth() {

        String logName = "my.log";
        String text = "Hello, world!";

        LogEntry entry =
            LogEntry.newBuilder(Payload.StringPayload.of(text))
                .setSeverity(Severity.ERROR)
                .setLogName(logName)
                .setResource(MonitoredResource.newBuilder("global").build())
                .build();

        // Writes the log entry asynchronously
        logging.write(Collections.singleton(entry));

        System.out.printf("Logged: %s%n", text);


        List<LogEntry> entries = new ArrayList<>();
        //entries.add(LogEntry.of(Payload.StringPayload.of("Entry payload")));
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("key", "value");
        jsonMap.put("message", "this is a message");
        //entries.add(LogEntry.of(Payload.JsonPayload.of(jsonMap)));
        entry = LogEntry.newBuilder(Payload.JsonPayload.of(jsonMap)).setSeverity(Severity.ERROR).build();
        entries.add(entry);
        logging.write(
            entries,
            Logging.WriteOption.logName(logName),
            Logging.WriteOption.resource(MonitoredResource.newBuilder("gae_app").build()));

        System.out.printf("Logged: end", text);

    }

    public static void main(String[] args) {

        cloudLogger.error("This is first log entry!", null);

        System.getProperties().setProperty(
                ClassicConstants.LOGBACK_CONTEXT_SELECTOR,
                ch.qos.logback.classic.LoggerContext.class.getName()
        );


        System.out.println("LoggerFactory: " + LoggerFactory.getILoggerFactory());

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);

        //logSmth();

        LogContext.setLabel("custom_label", "custom_value");
        LogContext.setJsonVar("custom_json_var", "custom_json_value");
        LogContext.setJsonVar("message", "This is a message injected to JSON");

        log.info("Starting the GS2BQ Example pipeline 1...");

        LogContext.clearJsonVar();

        LogContext.setLabel("custom_label", "custom_value_new");
        LogContext.setLabel("custom_label_2", "custom_value_new_2");

        log.info("label123=value123|Starting the GS2BQ Example pipeline 2...");

        log.error("label1=value1|label2=value2|Custom Labels example");
        log.error("beam_example=Numeric_Validation_Error|Number format validation");

        val options = PipelineOptionsFactory.fromArgs(args)
            .withValidation()
            .as(GsToBqOptions.class);
        options.setRunner(DataflowRunner.class);

        if (options.getTemplateLocation() != null) {
            log.info("Call for a template generation");
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
            log.info("Start processing");
            PnrGsToBqPipeline.createAndRunPipeline(options);
        }

        log.error("label314=value314|label2=value2|Custom Labels example");
        log.info("GS2BQ processing finished.");
    }
}
