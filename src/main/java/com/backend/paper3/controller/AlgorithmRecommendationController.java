package com.backend.paper3.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.dto.AlgorithmRecommendationDto;
import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.AlgorithmRecommendationService;

@RestController
@RequestMapping("/recommendations")
public class AlgorithmRecommendationController {

    @Autowired
    private AlgorithmRecommendationService algorithmRecommendationService;

    @PostMapping("/dataset/{datasetId}")
    public ApiResponse<AlgorithmRecommendationDto> recommendForDataset(
            @PathVariable Long datasetId
    ) {

        AlgorithmRecommendationDto response =
                algorithmRecommendationService
                        .recommendForDataset(datasetId);

        return ApiResponse
                .<AlgorithmRecommendationDto>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping
    public ApiResponse<List<AlgorithmRecommendationDto>> getAllRecommendations() {

        List<AlgorithmRecommendationDto> response =
                algorithmRecommendationService
                        .getAllRecommendations();

        return ApiResponse
                .<List<AlgorithmRecommendationDto>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping("/dataset/{datasetId}")
    public ApiResponse<List<AlgorithmRecommendationDto>> getRecommendationsByDatasetId(
            @PathVariable Long datasetId
    ) {

        List<AlgorithmRecommendationDto> response =
                algorithmRecommendationService
                        .getRecommendationsByDatasetId(datasetId);

        return ApiResponse
                .<List<AlgorithmRecommendationDto>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }
}