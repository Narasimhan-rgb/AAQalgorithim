package com.backend.paper3.dto;

public class PythonQuantumRequestDto {

    private String filePath;

    private String selectedColumn;

    private Integer sampleSize;

    private Integer candidateCount;

    private Double learningRate;

    private Double reinforcementStrength;

    private Double suppressionStrength;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(
            String filePath
    ) {
        this.filePath = filePath;
    }

    public String getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(
            String selectedColumn
    ) {
        this.selectedColumn = selectedColumn;
    }

    public Integer getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(
            Integer sampleSize
    ) {
        this.sampleSize = sampleSize;
    }

    public Integer getCandidateCount() {
        return candidateCount;
    }

    public void setCandidateCount(
            Integer candidateCount
    ) {
        this.candidateCount = candidateCount;
    }

    public Double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(
            Double learningRate
    ) {
        this.learningRate = learningRate;
    }

    public Double getReinforcementStrength() {
        return reinforcementStrength;
    }

    public void setReinforcementStrength(
            Double reinforcementStrength
    ) {
        this.reinforcementStrength = reinforcementStrength;
    }

    public Double getSuppressionStrength() {
        return suppressionStrength;
    }

    public void setSuppressionStrength(
            Double suppressionStrength
    ) {
        this.suppressionStrength = suppressionStrength;
    }
}