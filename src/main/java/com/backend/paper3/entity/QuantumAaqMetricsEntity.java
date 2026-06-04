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
@Table(name = "quantum_aaq_metrics")
@Getter
@Setter
public class QuantumAaqMetricsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "job_unique_id")
    private String jobUniqueId;

    @Column(name = "dataset_id", nullable = false)
    private Long datasetId;

    @Column(name = "dataset_name")
    private String datasetName;

    @Column(name = "dataset_unique_id")
    private String datasetUniqueId;

    @Column(name = "algorithm")
    private String algorithm;

    @Column(name = "comparison_count")
    private Long comparisonCount;

    @Column(name = "swap_count")
    private Long swapCount;

    @Column(name = "pivot_selection_count")
    private Long pivotSelectionCount;

    @Column(name = "insertion_sort_usage_count")
    private Long insertionSortUsageCount;

    @Column(name = "heap_fallback_count")
    private Long heapFallbackCount;

    @Column(name = "partition_count")
    private Long partitionCount;

    @Column(name = "average_partition_imbalance")
    private Double averagePartitionImbalance;

    @Column(name = "max_partition_imbalance")
    private Double maxPartitionImbalance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}