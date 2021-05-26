package com.dxc.poc.beam.utils.logging;

import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.*;
import lombok.val;
import org.apache.beam.sdk.values.KV;

import java.io.Serializable;
import java.util.Arrays;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;

public final class CloudLogger implements Serializable {

    private static final String DEFAULT_LOG_NAME = "my.log";
    private static final String GAE_APP = "gae_app";

    public static CloudLogger getLogger(Class<?> clazz) {
        return new CloudLogger(DEFAULT_LOG_NAME, clazz.getName());
    }

    private final String logName;
    private final String loggerName;

    private CloudLogger(String logName, String loggerName) {
        this.logName = logName;
        this.loggerName = loggerName;
    }

    private void logEvent(Severity severity, String message, KV<Object, Object>[] labels) {
        Logging logging = LoggingOptions.getDefaultInstance().getService();
        val builder = LogEntry.newBuilder(Payload.JsonPayload.of(singletonMap("message", message)))
            .setSeverity(severity)
            .addLabel("loggerName", this.loggerName);
        if (labels != null) {
            Arrays.stream(labels)
                .forEach(item -> builder.addLabel(String.valueOf(item.getKey()), String.valueOf(item.getValue())));
        }
        logging.write(
            singletonList(builder.build()),
            Logging.WriteOption.logName(logName),
            Logging.WriteOption.resource(MonitoredResource.newBuilder(GAE_APP).build()));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void debug(String message, KV... labels) {
        this.logEvent(Severity.DEBUG, message, labels);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void debug(String message, Throwable e, KV... labels) {
        this.logEvent(Severity.DEBUG, message + "\n" + e.toString(), labels);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void error(String message, KV... labels) {
        this.logEvent(Severity.ERROR, message, labels);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void error(String message, Throwable e, KV... labels) {
        this.logEvent(Severity.ERROR, message + "\n" + e.toString(), labels);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void info(String message, KV... labels) {
        this.logEvent(Severity.INFO, message, labels);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void info(String message, Throwable e, KV... labels) {
        this.logEvent(Severity.INFO, message + "\n" + e.toString(), labels);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void warn(String message, KV... labels) {
        this.logEvent(Severity.WARNING, message, labels);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void warn(String message, Throwable e, KV... labels) {
        this.logEvent(Severity.WARNING, message + "\n" + e.toString(), labels);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void critical(String message, KV... labels) {
        this.logEvent(Severity.CRITICAL, message, labels);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void critical(String message, Throwable e, KV... labels) {
        this.logEvent(Severity.CRITICAL, message + "\n" + e.toString(), labels);
    }
}
