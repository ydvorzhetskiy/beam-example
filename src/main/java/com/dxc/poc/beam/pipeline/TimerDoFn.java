package com.dxc.poc.beam.pipeline;

import com.sabre.gcp.logging.CloudLogger;
import org.apache.beam.sdk.state.*;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.joda.time.Duration;

public class TimerDoFn<T> extends DoFn<T, T> {

    @ProcessElement
    public void process(
        ProcessContext c
//        @TimerId("timer") Timer timer
    ) {
//        timer.offset(Duration.standardSeconds(60)).setRelative();
        c.output(c.element());
    }

//    @OnTimer("timer")
//    public void onTimer() {
//        log.info("App is available", KV.of("beam_example_status", "Alive"));
//    }
}
