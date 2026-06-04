package com.backend.paper3.service;

import com.backend.paper3.dto.BenchmarkComparisonSummaryDto;
import com.backend.paper3.dto.BenchmarkResultDto;

public interface BenchmarkComparisonService {

    BenchmarkComparisonSummaryDto compareAlgorithmsByDataset(
            Long datasetId
    );

    BenchmarkResultDto getBestResultByDataset(
            Long datasetId
    );
}