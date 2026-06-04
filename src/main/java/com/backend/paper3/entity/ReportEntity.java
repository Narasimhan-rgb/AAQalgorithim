package com.backend.paper3.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reports")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "dataset_id")
    private Long datasetId;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "report_type")
    private String reportType;

    @Column(name = "report_file_path", columnDefinition = "TEXT")
    private String reportFilePath;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(
            Long id
    ) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(
            Long jobId
    ) {
        this.jobId = jobId;
    }

    public Long getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(
            Long datasetId
    ) {
        this.datasetId = datasetId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(
            String reportName
    ) {
        this.reportName = reportName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(
            String reportType
    ) {
        this.reportType = reportType;
    }

    public String getReportFilePath() {
        return reportFilePath;
    }

    public void setReportFilePath(
            String reportFilePath
    ) {
        this.reportFilePath = reportFilePath;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(
            Long createdBy
    ) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }
}