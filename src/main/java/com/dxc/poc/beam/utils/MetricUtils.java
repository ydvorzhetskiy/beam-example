package com.dxc.poc.beam.utils;

import com.sabre.gcp.logging.CloudLogger;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
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

    public static <T extends PInput, OutputT extends POutput> PTransform<T, OutputT> rowCount(
        String metricName, PTransform<T, OutputT> original
    ) {
        return new RowsCountMetricsWrapper<>(metricName, original);
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

    private static class RowsCountMetricsWrapper<T extends PInput, OutputT extends POutput>
        extends PTransform<T, OutputT> {

        private final Counter counter;
        private final PTransform<T, OutputT> original;

        public RowsCountMetricsWrapper(
            String metricName, PTransform<T, OutputT> original
        ) {
            super(original.getName());
            this.original = original;
            this.counter = Metrics.counter("beam-example", metricName);
        }

        @Override
        public OutputT expand(T input) {
            this.counter.inc();
            return original.expand(input);
        }
    }

    public static class ElapsedTimeLabelWriteResultWrapper<T, OutputT extends POutput>
        extends PTransform<PCollection<T>, OutputT> {

        private final CloudLogger cloudLogger;
        private final String message;
        private final String label;
        private final PTransform<PCollection<T>, OutputT> originalPTransform;

        public ElapsedTimeLabelWriteResultWrapper(
            CloudLogger logger,
            String message,
            String labelName,
            PTransform<PCollection<T>, OutputT> originalPTransform
        ) {
            this.cloudLogger = logger;
            this.message = message;
            this.label = labelName;
            this.originalPTransform = originalPTransform;
        }

        @Override
        public OutputT expand(PCollection<T> input) {
            Instant start = Instant.now();
            val result = originalPTransform.expand(input);
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            this.cloudLogger.debug(message + " " + duration.toMillis() + " ms",
                KV.of(label, "" + duration.toMillis()));
            return result;
        }
    }
}
