package com.dxc.poc.beam.logback.enhancers;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.LoggingEnhancer;

public class CDMLogEnhancer implements LoggingEnhancer {
    @Override
    public void enhanceLogEntry(LogEntry.Builder builder) {
        builder.addLabel("JobName", "PNR_DATA_INGESTION");
    }
}
