package com.backend.paper3.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BenchmarkComparisonSummaryDto {

    private Long datasetId;

    private String datasetName;

    private String datasetUniqueId;

    private String bestAlgorithm;

    private Long bestExecutionTimeMs;

    private String aaqAlgorithm;

    private Long aaqExecutionTimeMs;

    private Double aaqThroughputRecordsPerSecond;

    private Long totalAlgorithmsCompared;

    private List<AlgorithmComparisonDto> comparisons;

    private LocalDateTime generatedAt;
}