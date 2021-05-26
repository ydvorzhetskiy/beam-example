package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.dto.Pnr;
import com.dxc.poc.beam.utils.logging.CloudLogger;
import lombok.val;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.extensions.jackson.ParseJsons;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.metrics.MetricNameFilter;
import org.apache.beam.sdk.metrics.MetricResult;
import org.apache.beam.sdk.metrics.MetricsFilter;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;

public class PnrGsToBqPipeline {

    private static final CloudLogger logger = CloudLogger.getLogger(PnrGsToBqPipeline.class);

    public static void createAndRunPipeline(GsToBqOptions options) {
        val tableRef = BqMetadataFactory.createTableReference(
            options.getProject(), options.getDataset(), options.getTableName());
        val schema = BqMetadataFactory.createTableSchema();

        val result = Pipeline.create(options)
            .apply("Read JSON from file",
                TextIO.read().from(options.getInputFile()))
            .apply("Parse JSON to DTO",
                ParseJsons.of(Pnr.class))
            .setCoder(SerializableCoder.of(Pnr.class))
            .apply("Convert to table row",
                ParDo.of(new ToTableRowDoFn()))
            .apply("Write to BQ", BigQueryIO.writeTableRows()
                .to(tableRef)
                .withSchema(schema)
                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                .withCustomGcsTempLocation(ValueProvider.StaticValueProvider.of(options.getGcpTempLocation())))
            .getPipeline()
            .run();

        result.waitUntilFinish();

        val metrics = result.metrics();

        // Write all successful processed
        val metricsResults = metrics.queryMetrics(MetricsFilter.builder()
            .addNameFilter(MetricNameFilter.named("beam-example", "good_record_count")).build());

        for (MetricResult<Long> counter : metricsResults.getCounters()) {
            // Here is only one counter
            val count = counter.getAttempted();
            logger.info("Number of good records: " + count,
                KV.of("stats", "good_records_processed"),
                KV.of("count", count));
        }
    }
}
