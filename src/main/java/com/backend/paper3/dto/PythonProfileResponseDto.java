package com.backend.paper3.dto;

public class PythonProfileResponseDto {

    private Long rowCount;

    private Integer columnCount;

    private String selectedColumn;

    private String dataType;

    private Double nullPercentage;

    private Double duplicatePercentage;

    private Double minValue;

    private Double maxValue;

    private Double mean;

    private Double median;

    private Double standardDeviation;

    private Double skewness;

    private Double sortednessScore;

    private String detectedPattern;

    public Long getRowCount() {
        return rowCount;
    }

    public void setRowCount(
            Long rowCount
    ) {
        this.rowCount = rowCount;
    }

    public Integer getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(
            Integer columnCount
    ) {
        this.columnCount = columnCount;
    }

    public String getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(
            String selectedColumn
    ) {
        this.selectedColumn = selectedColumn;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(
            String dataType
    ) {
        this.dataType = dataType;
    }

    public Double getNullPercentage() {
        return nullPercentage;
    }

    public void setNullPercentage(
            Double nullPercentage
    ) {
        this.nullPercentage = nullPercentage;
    }

    public Double getDuplicatePercentage() {
        return duplicatePercentage;
    }

    public void setDuplicatePercentage(
            Double duplicatePercentage
    ) {
        this.duplicatePercentage = duplicatePercentage;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(
            Double minValue
    ) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(
            Double maxValue
    ) {
        this.maxValue = maxValue;
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(
            Double mean
    ) {
        this.mean = mean;
    }

    public Double getMedian() {
        return median;
    }

    public void setMedian(
            Double median
    ) {
        this.median = median;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(
            Double standardDeviation
    ) {
        this.standardDeviation = standardDeviation;
    }

    public Double getSkewness() {
        return skewness;
    }

    public void setSkewness(
            Double skewness
    ) {
        this.skewness = skewness;
    }

    public Double getSortednessScore() {
        return sortednessScore;
    }

    public void setSortednessScore(
            Double sortednessScore
    ) {
        this.sortednessScore = sortednessScore;
    }

    public String getDetectedPattern() {
        return detectedPattern;
    }

    public void setDetectedPattern(
            String detectedPattern
    ) {
        this.detectedPattern = detectedPattern;
    }
}