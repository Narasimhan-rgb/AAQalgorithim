package com.backend.paper3.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlgorithmComparisonDto {

    private String algorithm;

    private Long executionTimeMs;

    private Long inputSize;

    private Long comparisonCount;

    private Long swapCount;

    private Double throughputRecordsPerSecond;

    private Double improvementPercentageVsAaq;

    private Boolean aaqFasterThanThisAlgorithm;

    private LocalDateTime createdAt;
}