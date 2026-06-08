package com.backend.paper3.dto;

public class PythonProfileRequestDto {

    private String filePath;

    private String selectedColumn;

    private Integer sampleSize;

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
}