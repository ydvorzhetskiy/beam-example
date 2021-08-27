package com.dxc.poc.beam;

import com.dxc.poc.beam.options.BqToBqOptions;
import com.dxc.poc.beam.pipeline.PnrBqToBqPipeline;
import lombok.val;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

public class BqToBqApplication {

    public static void main(String[] args) {
        val options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(BqToBqOptions.class);
            options.setRunner(DataflowRunner.class);
        PnrBqToBqPipeline.createAndRunPipeline(options);
    }
}
