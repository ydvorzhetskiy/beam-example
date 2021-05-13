package com.dxc.poc.beam;

import com.dxc.poc.beam.pipeline.PnrPubSubToBqPipeline;
import com.dxc.poc.beam.pipeline.PubSubToBqOptions;
import lombok.val;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

public class PubSubToBqApplication {

    public static void main(String[] args) {
        val options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(PubSubToBqOptions.class);

        PnrPubSubToBqPipeline.createAndRunPipeline(options);
    }
}
