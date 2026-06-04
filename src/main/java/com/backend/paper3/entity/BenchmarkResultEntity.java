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
@Table(name = "benchmark_results")
@Getter
@Setter
public class BenchmarkResultEntity {

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

    @Column(name = "selected_column")
    private String selectedColumn;

    @Column(name = "input_size")
    private Long inputSize;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "comparison_count")
    private Long comparisonCount;

    @Column(name = "swap_count")
    private Long swapCount;

    @Column(name = "throughput_records_per_second")
    private Double throughputRecordsPerSecond;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}