package com.backend.paper3.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BenchmarkResultDto {

    private Long id;

    private Long jobId;

    private String jobUniqueId;

    private Long datasetId;

    private String datasetName;

    private String datasetUniqueId;

    private String algorithm;

    private String selectedColumn;

    private Long inputSize;

    private Long executionTimeMs;

    private Long comparisonCount;

    private Long swapCount;

    private Double throughputRecordsPerSecond;

    private String status;

    private LocalDateTime createdAt;
}