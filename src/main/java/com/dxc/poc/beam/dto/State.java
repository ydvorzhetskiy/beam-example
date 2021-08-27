package com.dxc.poc.beam.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedList;

@Data
@Accessors(chain = true)
public class State implements Serializable {

    private LinkedList<MyData> data;
}
