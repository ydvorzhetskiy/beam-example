package com.dxc.poc.beam.transforms;

import com.dxc.poc.beam.dto.MyData;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

public class FakeKvPair extends DoFn<MyData, KV<String, MyData>> {
    @DoFn.ProcessElement
    public void processElement(ProcessContext c) {
        c.output(KV.of("", c.element()));
    }
}

