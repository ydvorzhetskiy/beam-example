package com.dxc.poc.beam.pipeline;

import com.dxc.poc.beam.GsToBqApplication;
import com.dxc.poc.beam.dto.Pnr;
import com.sabre.gcp.logging.CloudLogger;
import lombok.val;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.extensions.jackson.ParseJsons;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;

import static com.dxc.poc.beam.utils.MetricUtils.ElapsedTimeLabelWriteResultWrapper;
import static com.dxc.poc.beam.utils.MetricUtils.rowCount;

public class PnrPubSubToBqPipeline {

    private static final CloudLogger LOG = CloudLogger.getLogger(GsToBqApplication.class,
        "ps-to-bq-job",
        KV.of("application-name", "beam-example"));

    public static void createAndRunPipeline(PubSubToBqOptions options) {
        try {
            LOG.info("Pipeline starting", KV.of("pipeline_status", "starting"));
            val tableRef = BqMetadataFactory.createTableReference(
                options.getProject(), options.getDataset(), options.getTableName());
            val schema = BqMetadataFactory.createTableSchema();

            Pipeline.create(options)
                .apply("Read JSON from Pub/Sub",
                    PubsubIO.readStrings().fromTopic(options.getInputTopic()))
                .apply("Parse JSON to DTO", rowCount("total_rows", ParseJsons.of(Pnr.class)))
                .setCoder(SerializableCoder.of(Pnr.class))
                .apply(ParDo.of(new TimerDoFn<>()))
                .apply("Convert to table row", ParDo.of(new ToTableRowDoFn()))
                .apply("Write to BQ", new ElapsedTimeLabelWriteResultWrapper<>(
                    LOG, "Write to BQ took", "write_to_bq_stat",
                    BigQueryIO.writeTableRows()
                        .to(tableRef)
                        .withSchema(schema)
                        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                        .withCustomGcsTempLocation(ValueProvider.StaticValueProvider.of(options.getGcpTempLocation()))))
                .getPipeline()
                .run().waitUntilFinish();
        } catch (Exception ex) {
            LOG.error("Pipeline error", ex, KV.of("pipeline_status", "error"));
        }
    }
}
