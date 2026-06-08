package com.backend.paper3.service;

import com.backend.paper3.dto.PythonProfileRequestDto;
import com.backend.paper3.dto.PythonProfileResponseDto;

public interface PythonProfilerClient {

    PythonProfileResponseDto profileDataset(
            PythonProfileRequestDto request
    );
}