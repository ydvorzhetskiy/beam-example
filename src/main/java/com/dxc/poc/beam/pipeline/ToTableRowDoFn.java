package com.dxc.poc.beam.pipeline;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import com.dxc.poc.beam.GsToBqApplication;
import com.dxc.poc.beam.dto.Pnr;
import com.google.api.services.bigquery.model.TableRow;
import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class ToTableRowDoFn extends DoFn<Pnr, TableRow> {

    static {
        System.getProperties().setProperty(
                ClassicConstants.LOGBACK_CONTEXT_SELECTOR,
                ch.qos.logback.classic.LoggerContext.class.getName()
        );
    }

    static void logError(KV<String,String>[] label, String message) {

        Logging logging = LoggingOptions.getDefaultInstance().getService();

        String logName = "my.log";
//        String text = "Hello, world!";
//
//        LogEntry entry =
//                LogEntry.newBuilder(Payload.StringPayload.of(text))
//                        .setSeverity(Severity.ERROR)
//                        .setLogName(logName)
//                        .setResource(MonitoredResource.newBuilder("global").build())
//                        .build();
//
//        // Writes the log entry asynchronously
//        logging.write(Collections.singleton(entry));
//
//        System.out.printf("Logged: %s%n", text);


        List<LogEntry> entries = new ArrayList<>();
        //entries.add(LogEntry.of(Payload.StringPayload.of("Entry payload")));
        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put(label.getKey(), label.getValue());
        jsonMap.put("message", message);
        //entries.add(LogEntry.of(Payload.JsonPayload.of(jsonMap)));
        val builder = LogEntry.newBuilder(Payload.JsonPayload.of(jsonMap)).
                setSeverity(Severity.ERROR);
        if(label!=null){
            Arrays.stream(label).forEach(item -> builder.addLabel(item.getKey(), item.getValue()));
        }
        LogEntry entry = builder.build();
        entries.add(entry);
        logging.write(
                entries,
                Logging.WriteOption.logName(logName),
                Logging.WriteOption.resource(MonitoredResource.newBuilder("gae_app").build()));


    }

    //private static Logger log = LoggerFactory.getLogger(ToTableRowDoFn.class);

    private Counter goodRecordCounter = Metrics.counter("beam-example", "good_record_count");
    private Counter badRecordCounter = Metrics.counter("beam-example", "bad_record_count");

    @ProcessElement
    public void processElement(@Element Pnr pnr, OutputReceiver<TableRow> out) {

        System.getProperties().setProperty(
                ClassicConstants.LOGBACK_CONTEXT_SELECTOR,
                ch.qos.logback.classic.LoggerContext.class.getName()
        );

        Logger log = LoggerFactory.getLogger(ToTableRowDoFn.class);

        try {
            val row = PnrConverter.toTableRow(pnr);
            out.output(row);
            goodRecordCounter.inc();
        } catch (NumberFormatException e){

//            System.out.println("===============================================");
//            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//            StatusPrinter.print(lc);

//            ch.qos.logback.classic.LoggerContext ctx = new LoggerContext();
//            val altLogger = ctx.getLogger(ToTableRowDoFn.class);

            badRecordCounter.inc();
            logError(new KV[]{KV.of("beam_example", "Numeric_Validation_Error")}, "Number format validation log record");

//            GsToBqApplication.log.error("beam_example=Numeric_Validation_Error_root|Number format validation");
//            log.error("beam_example=Numeric_Validation_Error|Number format validation");
//            altLogger.error("beam_example=Numeric_Validation_Error_Alt|Number format validation");
        } catch (Exception e) {
            log.error("Unexpected exception:", e);
        }
    }
}
