package com.backend.paper3.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlgorithmRecommendationDto {

    private Long id;

    private Long datasetId;

    private String datasetName;

    private String datasetUniqueId;

    private String datasetPattern;

    private Long recordCount;

    private Double duplicatePercentage;

    private Double skewnessValue;

    private Double sortednessScore;

    private String recommendedAlgorithm;

    private Double confidenceScore;

    private String recommendationReason;

    private LocalDateTime createdAt;
}