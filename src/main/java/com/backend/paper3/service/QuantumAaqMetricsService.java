package com.backend.paper3.service;

import java.util.List;

import com.backend.paper3.algorithm.AAQMetrics;
import com.backend.paper3.dto.QuantumAaqMetricsDto;
import com.backend.paper3.dto.SortingRunResultDto;

public interface QuantumAaqMetricsService {

    QuantumAaqMetricsDto saveAaqMetrics(
            SortingRunResultDto sortingResult,
            AAQMetrics metrics
    );

    List<QuantumAaqMetricsDto> getMetricsByJobId(
            Long jobId
    );

    List<QuantumAaqMetricsDto> getMetricsByDatasetId(
            Long datasetId
    );
}