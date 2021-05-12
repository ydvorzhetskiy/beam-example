package com.dxc.poc.beam.pipeline;

import lombok.val;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;

public class FileToFilePipeline {

    public static void createAndRunPipeline(FileToFileOptions options) {
        Pipeline.create(options)
                .apply(TextIO.read().from(options.getInputFile()))
                .apply(TextIO.write().to(options.getOutputFile()))
                .getPipeline()
                .run().waitUntilFinish();
    }
}
