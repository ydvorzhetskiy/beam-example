package com.dxc.poc.beam.utils.logging;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.LoggingEnhancer;
import com.google.cloud.logging.Payload;

public class CustomLogEnhancer implements LoggingEnhancer {

    @Override
    public void enhanceLogEntry(LogEntry.Builder builder) {

        LogContext.getLabels().forEach((k, v) -> builder.addLabel(k, v));

        if(!LogContext.getJsonVars().isEmpty()) {
            builder.setPayload(Payload.JsonPayload.of(LogContext.getJsonVars()));
        }

    }
}
