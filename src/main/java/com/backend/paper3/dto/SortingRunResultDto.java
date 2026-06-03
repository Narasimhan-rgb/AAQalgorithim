package com.backend.paper3.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.backend.paper3.enums.SortingJobStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortingRunResultDto {

    private Long jobId;

    private String jobUniqueId;

    private Long datasetId;

    private String datasetName;

    private String datasetUniqueId;

    private String algorithm;

    private String selectedColumn;

    private Long totalValuesSorted;

    private Long executionTimeMs;

    private SortingJobStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private List<String> sortedPreview;
}