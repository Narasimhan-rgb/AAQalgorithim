package com.backend.paper3.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.dto.SortingRunRequestDto;
import com.backend.paper3.dto.SortingRunResultDto;
import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.SortingExecutionService;

@RestController
@RequestMapping("/sorting")
public class SortingExecutionController {

    @Autowired
    private SortingExecutionService sortingExecutionService;

    @PostMapping("/run")
    public ApiResponse<SortingRunResultDto> runSorting(
            @RequestBody SortingRunRequestDto request
    ) {

        SortingRunResultDto response =
                sortingExecutionService.runSorting(request);

        return ApiResponse
                .<SortingRunResultDto>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }
}