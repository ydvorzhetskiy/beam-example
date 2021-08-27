package com.dxc.poc.beam.dto;

import com.google.api.services.bigquery.model.TableRow;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MyData implements Serializable {

    private String field_a;
    private String field_b;

    public static MyData fromTableRow(TableRow tableRow) {
        return new MyData().setField_a((String) tableRow.get("field_a"))
                .setField_b((String) tableRow.get("field_b"));
    }
}
