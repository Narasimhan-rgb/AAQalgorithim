package com.backend.paper3.entity;

import java.time.LocalDateTime;

import com.backend.paper3.enums.DatasetPattern;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "datasets")
@Getter
@Setter
public class DatasetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataset_name", nullable = false)
    private String datasetName;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_path", columnDefinition = "TEXT")
    private String filePath;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "record_count")
    private Long recordCount;

    @Column(name = "column_count")
    private Integer columnCount;

    @Column(name = "selected_sort_column")
    private String selectedSortColumn;

    @Column(name = "data_type")
    private String dataType;

    @Enumerated(EnumType.STRING)
    @Column(name = "detected_pattern")
    private DatasetPattern detectedPattern;

    @Column(name = "value")
    private Double value;

    @Column(name = "duplicate_percentage")
    private Double duplicatePercentage;

    @Column(name = "null_percentage")
    private Double nullPercentage;

    @Column(name = "skewness_value")
    private Double skewnessValue;

    @Column(name = "sortedness_score")
    private Double sortednessScore;

    @Column(name = "quantum_score")
    private Double quantumScore;

    @Column(name = "final_score")
    private Double finalScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}