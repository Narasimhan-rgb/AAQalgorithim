package com.backend.paper3.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "algorithm_recommendations")
@Getter
@Setter
public class AlgorithmRecommendationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataset_id", nullable = false)
    private Long datasetId;

    @Column(name = "dataset_name")
    private String datasetName;

    @Column(name = "dataset_unique_id")
    private String datasetUniqueId;

    @Column(name = "dataset_pattern")
    private String datasetPattern;

    @Column(name = "record_count")
    private Long recordCount;

    @Column(name = "duplicate_percentage")
    private Double duplicatePercentage;

    @Column(name = "skewness_value")
    private Double skewnessValue;

    @Column(name = "sortedness_score")
    private Double sortednessScore;

    @Column(name = "recommended_algorithm")
    private String recommendedAlgorithm;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "recommendation_reason", columnDefinition = "TEXT")
    private String recommendationReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}