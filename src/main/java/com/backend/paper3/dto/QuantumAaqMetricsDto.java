package com.backend.paper3.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuantumAaqMetricsDto {

    private Long id;

    private Long jobId;

    private String jobUniqueId;

    private Long datasetId;

    private String datasetName;

    private String datasetUniqueId;

    private String algorithm;

    private Long comparisonCount;

    private Long swapCount;

    private Long pivotSelectionCount;

    private Long insertionSortUsageCount;

    private Long heapFallbackCount;

    private Long partitionCount;

    private Double averagePartitionImbalance;

    private Double maxPartitionImbalance;

    private LocalDateTime createdAt;
}