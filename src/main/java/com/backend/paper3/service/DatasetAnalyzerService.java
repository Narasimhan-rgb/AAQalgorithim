package com.backend.paper3.service;

import com.backend.paper3.dto.PythonProfileResponseDto;

public interface DatasetAnalyzerService {

    PythonProfileResponseDto analyzeDataset(
            Long datasetId
    );
}