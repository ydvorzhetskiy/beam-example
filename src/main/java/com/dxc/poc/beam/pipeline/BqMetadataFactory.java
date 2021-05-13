package com.dxc.poc.beam.pipeline;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableSchema;
import lombok.val;

import java.util.ArrayList;

public class BqMetadataFactory {

    public static TableReference createTableReference(
            String projectId, String dataset, String tableName
    ) {
        val tableRef = new TableReference();
        tableRef.setProjectId(projectId);
        tableRef.setDatasetId(dataset);
        tableRef.setTableId(tableName);
        return tableRef;
    }

    public static TableSchema createTableSchema() {
        val fieldDefs = new ArrayList<TableFieldSchema>();
        fieldDefs.add(new TableFieldSchema().setName("pr_locator_id").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("ticket_number").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("pr_create_date").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("pr_sequence").setType("NUMERIC"));
        fieldDefs.add(new TableFieldSchema().setName("from_datetime").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("tr_datetime").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("creditcard_network").setType("STRING"));
        fieldDefs.add(new TableFieldSchema().setName("creditcard_number").setType("STRING"));
        return new TableSchema().setFields(fieldDefs);
    }
}
