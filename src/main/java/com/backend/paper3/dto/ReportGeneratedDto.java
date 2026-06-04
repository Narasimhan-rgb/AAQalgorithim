package com.backend.paper3.dto;

import java.time.LocalDateTime;

public class ReportGeneratedDto {

    private Long id;

    private Long jobId;

    private Long datasetId;

    private String reportName;

    private String reportType;

    private String reportFilePath;

    private Long createdBy;

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