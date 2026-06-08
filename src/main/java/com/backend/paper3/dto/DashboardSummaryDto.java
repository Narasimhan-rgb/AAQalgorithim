package com.backend.paper3.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardSummaryDto {

    private Long totalDatasets;

    private Long totalSortingJobs;

    private Long pendingJobs;

    private Long runningJobs;

    private Long completedJobs;

    private Long failedJobs;

    private Long cancelledJobs;

    private Long totalBenchmarkResults;

    private Long totalRecommendations;

    private Long totalQuantumMetricRecords;

    private Long bestAaqExecutionTimeMs;

    private Double averageThroughputRecordsPerSecond;

    private String latestRecommendedAlgorithm;

    private Double latestRecommendationConfidence;

    private String latestRecommendationReason;

    private Long latestQuantumJobId;

    private Long latestPivotSelectionCount;

    private Long latestInsertionSortUsageCount;

    private Long latestHeapFallbackCount;

    private Long latestPartitionCount;

    private Double latestAveragePartitionImbalance;

    private Double latestMaxPartitionImbalance;

    private Long latestQuantumDatasetId;

    private String latestQuantumDatasetName;

    private Double latestSimulatedPivotValue;

    private Double latestSimulatedBestPartitionImbalance;

    private Double latestSimulatedInterferenceGain;

    private Double latestSimulatedAmplitudeConvergenceScore;

    private Integer latestOpenQasmQubitCount;

    private String latestQuantumSimulationStatus;

    private LocalDateTime generatedAt;
}