package com.backend.paper3.dto;

import java.time.LocalDateTime;

import com.backend.paper3.enums.SortingJobStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortingJobDto {

    private Long id;

    private String jobUniqueId;

    private Long datasetId;

    private String datasetName;

    private String datasetUniqueId;

    private String algorithm;

    private String requestedAlgorithm;

    private String recommendedAlgorithm;

    private SortingJobStatus status;

    private Integer progressPercentage;

    private String selectedColumn;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private Long createdBy;

    private LocalDateTime createdAt;

    @JsonIgnore
    private String errorMessage;
}