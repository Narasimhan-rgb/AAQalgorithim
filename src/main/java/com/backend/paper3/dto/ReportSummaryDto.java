package com.backend.paper3.dto;

import java.time.LocalDateTime;

import com.backend.paper3.enums.DatasetPattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportSummaryDto {

    private Long datasetId;

    private String datasetUniqueId;

    private String datasetName;

    private String originalFileName;

    private String fileType;

    private Long fileSizeBytes;

    private Long recordCount;

    private Integer columnCount;

    private String selectedSortColumn;

    private String dataType;

    private DatasetPattern detectedPattern;

    private Double duplicatePercentage;

    private Double nullPercentage;

    private Double skewnessValue;

    private Double sortednessScore;

    private Double quantumScore;

    private Double finalScore;

    private AlgorithmRecommendationDto latestRecommendation;

    private BenchmarkComparisonSummaryDto benchmarkComparison;

    private BenchmarkResultDto bestBenchmarkResult;

    private QuantumAaqMetricsDto latestQuantumMetrics;

    private String reportStatus;

    private String reportMessage;

    private LocalDateTime generatedAt;
}