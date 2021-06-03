package com.dxc.poc.beam.utils;

import lombok.experimental.UtilityClass;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PInput;
import org.apache.beam.sdk.values.POutput;

import java.time.Duration;
import java.time.Instant;

@UtilityClass
public class MetricUtils {

    public static <T extends PInput, OutputT extends POutput> PTransform<T, OutputT> withElapsedTime(
        String metricName, PTransform<T, OutputT> original
    ) {
        return new ElapsedTimeMetricsWrapper<>(metricName, original);
    }

    private static class ElapsedTimeMetricsWrapper<T extends PInput, OutputT extends POutput>
        extends PTransform<T, OutputT> {

        private final Counter counter;
        private final PTransform<T, OutputT> original;

        public ElapsedTimeMetricsWrapper(
            String metricName, PTransform<T, OutputT> original
        ) {
            super(original.getName());
            this.original = original;
            this.counter = Metrics.counter("beam-example", metricName);
        }

        @Override
        public OutputT expand(T input) {
            Instant start = Instant.now();
            OutputT result = original.expand(input);
            Instant end = Instant.now();
            this.counter.inc(Duration.between(start, end).toMillis());
            return result;
        }
    }
}
