package com.dxc.poc.beam.utils.logging;

import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.*;
import lombok.val;
import org.apache.beam.sdk.values.KV;

import java.io.Serializable;
import java.util.*;

public final class CloudLogger implements Serializable {

    private static final String DEFAULT_LOG_NAME = "my.log";

    public static CloudLogger getLogger(Class<?> clazz) {
        return new CloudLogger(DEFAULT_LOG_NAME, clazz.getName());
    }

    private final String logName;
    private final String loggerName;

    private CloudLogger(String logName, String loggerName) {
        this.logName = logName;
        this.loggerName = loggerName;
    }

    private void logEvent(Severity severity, String message, KV<String, String>[] label) {
        // TODO: use loggerName
        Logging logging = LoggingOptions.getDefaultInstance().getService();
        List<LogEntry> entries = new ArrayList<>();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("message", message);
        val builder = LogEntry.newBuilder(Payload.JsonPayload.of(jsonMap)).
            setSeverity(severity);
        if (label != null) {
            Arrays.stream(label).forEach(item -> builder.addLabel(item.getKey(), item.getValue()));
        }
        LogEntry entry = builder.build();
        entries.add(entry);
        logging.write(
            entries,
            Logging.WriteOption.logName(logName),
            Logging.WriteOption.resource(MonitoredResource.newBuilder("gae_app").build()));
    }

    public void error(String message, KV<String, String>[] labels) {
        this.logEvent(Severity.ERROR, message, labels);
    }
}
