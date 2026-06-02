package com.backend.paper3.entity;

import java.time.LocalDateTime;

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

    private String datasetName;

    private String originalFileName;

    private String filePath;

    private String fileType;

    private Long fileSizeBytes;

    private Integer recordCount;

    private Integer columnCount;

    private Double value;

    private Double duplicatePercentage;

    private Double nullPercentage;

    private Double sortednessScore;

    private Double quantumScore;

    private Double finalScore;

    private LocalDateTime createdAt;
}