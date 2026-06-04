package com.backend.paper3.service;

import java.util.List;

import com.backend.paper3.dto.AlgorithmRecommendationDto;

public interface AlgorithmRecommendationService {

    AlgorithmRecommendationDto recommendForDataset(
            Long datasetId
    );

    List<AlgorithmRecommendationDto> getAllRecommendations();

    List<AlgorithmRecommendationDto> getRecommendationsByDatasetId(
            Long datasetId
    );
}