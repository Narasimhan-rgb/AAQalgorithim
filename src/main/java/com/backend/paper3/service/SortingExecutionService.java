package com.backend.paper3.service;

import com.backend.paper3.dto.SortingRunRequestDto;
import com.backend.paper3.dto.SortingRunResultDto;

public interface SortingExecutionService {

    SortingRunResultDto runSorting(
            SortingRunRequestDto request
    );
}