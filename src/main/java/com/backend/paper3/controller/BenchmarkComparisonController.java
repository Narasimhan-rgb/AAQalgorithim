package com.backend.paper3.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.dto.BenchmarkComparisonSummaryDto;
import com.backend.paper3.dto.BenchmarkResultDto;
import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.BenchmarkComparisonService;

@RestController
@RequestMapping("/benchmarks")
public class BenchmarkComparisonController {

    @Autowired
    private BenchmarkComparisonService benchmarkComparisonService;

    @GetMapping("/compare/dataset/{datasetId}")
    public ApiResponse<BenchmarkComparisonSummaryDto> compareAlgorithmsByDataset(
            @PathVariable Long datasetId
    ) {

        BenchmarkComparisonSummaryDto response =
                benchmarkComparisonService
                        .compareAlgorithmsByDataset(
                                datasetId
                        );

        return ApiResponse
                .<BenchmarkComparisonSummaryDto>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping("/best/dataset/{datasetId}")
    public ApiResponse<BenchmarkResultDto> getBestResultByDataset(
            @PathVariable Long datasetId
    ) {

        BenchmarkResultDto response =
                benchmarkComparisonService
                        .getBestResultByDataset(
                                datasetId
                        );

        return ApiResponse
                .<BenchmarkResultDto>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }
}