package com.dxc.poc.beam.transforms;

import com.dxc.poc.beam.dto.MyData;
import org.apache.beam.sdk.transforms.DoFn;
import org.joda.time.Instant;

import java.util.Random;

public class AddTimestamp extends DoFn<MyData, MyData> {

    //This step needs, because data bounded source have same timestamps. I wanted to add timestamps in such way
    // that elements will be several fixed time windows
    @ProcessElement
    public void processElement(@DoFn.Element MyData element, OutputReceiver<MyData> out) {
        if (new Random().nextInt(10) % 10 == 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        out.outputWithTimestamp(element, Instant.now());
    }
}

