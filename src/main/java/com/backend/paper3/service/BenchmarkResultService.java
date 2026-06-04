package com.backend.paper3.service;

import java.util.List;

import com.backend.paper3.dto.BenchmarkResultDto;
import com.backend.paper3.dto.SortingRunResultDto;

public interface BenchmarkResultService {

    BenchmarkResultDto saveSortingRunResult(
            SortingRunResultDto result
    );

    List<BenchmarkResultDto> getAllResults();

    List<BenchmarkResultDto> getResultsByJobId(
            Long jobId
    );

    List<BenchmarkResultDto> getResultsByDatasetId(
            Long datasetId
    );
}