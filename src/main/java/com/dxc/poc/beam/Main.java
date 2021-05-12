package com.dxc.poc.beam;

import com.dxc.poc.beam.pipeline.FileToFileOptions;
import com.dxc.poc.beam.pipeline.FileToFilePipeline;
import lombok.val;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

public class Main {

    public static void main(String[] args) {
        val options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(FileToFileOptions.class);

        FileToFilePipeline.createAndRunPipeline(options);
    }
}
