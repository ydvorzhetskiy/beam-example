package com.dxc.poc.beam.transforms;

import com.dxc.poc.beam.dto.MyData;
import com.dxc.poc.beam.dto.State;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.state.StateSpec;
import org.apache.beam.sdk.state.StateSpecs;
import org.apache.beam.sdk.state.ValueState;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import java.util.LinkedList;

@Slf4j
public class StatefulDoFn extends DoFn<KV<String, MyData>, MyData> {

    @StateId("state")
    private final StateSpec<ValueState<State>> modelSpec = StateSpecs.value(SerializableCoder.of(State.class));

    @ProcessElement
    public void processElement(ProcessContext c, @StateId("state") ValueState<State> valueState) {
        State state = valueState.read();

        if (state == null) {
            state = new State();
        }
        if (state.getData() == null) {
            state.setData(new LinkedList<>());
        }
        state.getData().addLast(c.element().getValue());
        valueState.write(state);

        c.output(c.element().getValue());
        // We need to check in logs, that size is changing, but not too often
        if (state.getData().size() % 100 == 0) {
            log.info("Number of elements {}", state.getData().size());
            log.info("Current element timestamp {}", c.timestamp());
        }
    }
}