package com.dxc.poc.beam.logback.enhancers;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.LoggingEnhancer;
import com.google.cloud.logging.Payload;
import lombok.val;

import java.util.HashMap;

public class CDMLogEnhancer implements LoggingEnhancer {
    @Override
    public void enhanceLogEntry(LogEntry.Builder builder) {



        val payload = builder.build().getPayload();

        if(payload.getType().equals(Payload.Type.JSON)){
            Payload.JsonPayload jsonPayload = (Payload.JsonPayload) payload;
            String outputMessage = null;

            val message = (String) jsonPayload.getDataAsMap().get("message");

            if(message != null){
                val labels = message.split("\\|");

                if(labels.length > 1){

                    //last value is a message
                    outputMessage = labels[labels.length - 1];

                    for(int i=0; i < labels.length-1; ++i){

                        val pair = labels[i].split("=");

                        if(pair.length == 2){
                            builder.addLabel(pair[0], pair[1]);
                        }
                    }
                }
            }

            if(outputMessage != null){
                val jsonMap = new HashMap<String, Object>();
                jsonMap.putAll(jsonPayload.getDataAsMap());
                jsonMap.replace("message", outputMessage);
                builder.setPayload(Payload.JsonPayload.of(jsonMap));
            }
        }

//        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        builder.addLabel("JobName", "PNR_DATA_INGESTION_AFTER_BUILD");
    }
}
