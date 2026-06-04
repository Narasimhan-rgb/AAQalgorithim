package com.backend.paper3.algorithm;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortingAlgorithmResult {

    private List<String> sortedValues;

    private Long executionTimeMs;

    private Long comparisonCount;

    private Long swapCount;

    private Long inputSize;

    private String algorithmName;

    private AAQMetrics aaqMetrics;
}