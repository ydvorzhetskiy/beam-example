package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.Pnr;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.extensions.jackson.ParseJsons;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.TypeDescriptors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class FileToFilePipeline {

    static class GetCreditCard extends DoFn<Pnr, String> {
        @ProcessElement
        public void processElement(@Element Pnr pnr, OutputReceiver<String> out) {
            out.output(pnr.getCreditCards().get(0).getCardNumber());
        }
    }

    public static void createAndRunPipeline(FileToFileOptions options) {
        Pipeline.create(options)
                .apply(TextIO.read().from(options.getInputFile()))
                .apply(ParseJsons.of(Pnr.class))
                .setCoder(SerializableCoder.of(Pnr.class))
                .apply(ParDo.of(new GetCreditCard()))
                .apply(TextIO.write().to(options.getOutputFile()))
                .getPipeline()
                .run().waitUntilFinish();
    }
}
