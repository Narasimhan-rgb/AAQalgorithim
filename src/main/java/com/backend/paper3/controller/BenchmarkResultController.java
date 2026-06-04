package com.backend.paper3.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.dto.BenchmarkResultDto;
import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.BenchmarkResultService;

@RestController
@RequestMapping("/benchmarks")
public class BenchmarkResultController {

    @Autowired
    private BenchmarkResultService benchmarkResultService;

    @GetMapping
    public ApiResponse<List<BenchmarkResultDto>> getAllResults() {

        List<BenchmarkResultDto> response =
                benchmarkResultService.getAllResults();

        return ApiResponse
                .<List<BenchmarkResultDto>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping("/job/{jobId}")
    public ApiResponse<List<BenchmarkResultDto>> getResultsByJobId(
            @PathVariable Long jobId
    ) {

        List<BenchmarkResultDto> response =
                benchmarkResultService.getResultsByJobId(jobId);

        return ApiResponse
                .<List<BenchmarkResultDto>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping("/dataset/{datasetId}")
    public ApiResponse<List<BenchmarkResultDto>> getResultsByDatasetId(
            @PathVariable Long datasetId
    ) {

        List<BenchmarkResultDto> response =
                benchmarkResultService.getResultsByDatasetId(datasetId);

        return ApiResponse
                .<List<BenchmarkResultDto>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }
}