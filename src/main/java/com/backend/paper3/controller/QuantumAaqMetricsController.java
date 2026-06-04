package com.backend.paper3.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.dto.QuantumAaqMetricsDto;
import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.QuantumAaqMetricsService;

@RestController
@RequestMapping("/quantum-metrics")
public class QuantumAaqMetricsController {

    @Autowired
    private QuantumAaqMetricsService quantumAaqMetricsService;

    @GetMapping("/job/{jobId}")
    public ApiResponse<List<QuantumAaqMetricsDto>> getMetricsByJobId(
            @PathVariable Long jobId
    ) {

        List<QuantumAaqMetricsDto> response =
                quantumAaqMetricsService.getMetricsByJobId(jobId);

        return ApiResponse
                .<List<QuantumAaqMetricsDto>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping("/dataset/{datasetId}")
    public ApiResponse<List<QuantumAaqMetricsDto>> getMetricsByDatasetId(
            @PathVariable Long datasetId
    ) {

        List<QuantumAaqMetricsDto> response =
                quantumAaqMetricsService.getMetricsByDatasetId(datasetId);

        return ApiResponse
                .<List<QuantumAaqMetricsDto>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }
}