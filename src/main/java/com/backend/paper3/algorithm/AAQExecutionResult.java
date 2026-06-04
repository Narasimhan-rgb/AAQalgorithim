package com.backend.paper3.algorithm;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AAQExecutionResult {

    private List<String> sortedValues;

    private Long executionTimeMs;

    private Long inputSize;

    private AAQMetrics metrics;
}