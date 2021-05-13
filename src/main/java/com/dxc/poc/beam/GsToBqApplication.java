package com.dxc.poc.beam;

import com.dxc.poc.beam.pipeline.GsToBqOptions;
import com.dxc.poc.beam.pipeline.PnrGsToBqPipeline;
import lombok.val;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

public class GsToBqApplication {

    public static void main(String[] args) {
        val options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(GsToBqOptions.class);

        PnrGsToBqPipeline.createAndRunPipeline(options);
    }
}
