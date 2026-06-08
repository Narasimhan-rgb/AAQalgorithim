package com.backend.paper3.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemValidationDto {

    private String backendStatus;

    private String databaseStatus;

    private String pythonServiceStatus;

    private String pythonServiceBaseUrl;

    private Map<String, Object> pythonHealthResponse;

    private Long totalDatasets;

    private Long totalSortingJobs;

    private Long totalBenchmarkResults;

    private Long totalRecommendations;

    private Long totalQuantumMetricRecords;

    private String overallStatus;

    private String message;

    private LocalDateTime generatedAt;
}