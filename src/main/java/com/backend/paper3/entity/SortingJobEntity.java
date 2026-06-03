package com.backend.paper3.entity;

import java.time.LocalDateTime;

import com.backend.paper3.enums.SortingJobStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sorting_jobs")
@Getter
@Setter
public class SortingJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_unique_id", unique = true, length = 100)
    private String jobUniqueId;

    @Column(name = "dataset_id", nullable = false)
    private Long datasetId;

    @Column(name = "requested_algorithm")
    private String requestedAlgorithm;

    @Column(name = "recommended_algorithm")
    private String recommendedAlgorithm;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SortingJobStatus status;

    @Column(name = "progress_percentage")
    private Integer progressPercentage;

    @Column(name = "selected_column")
    private String selectedColumn;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}