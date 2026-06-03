package com.backend.paper3.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.dto.SortingJobDto;
import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.SortingJobService;

@RestController
@RequestMapping("/jobs")
public class SortingJobController {

    @Autowired
    private SortingJobService sortingJobService;

    @PostMapping("/start")
    public ApiResponse<SortingJobDto> startJob(
            @RequestBody SortingJobDto dto
    ) {

        SortingJobDto response =
                sortingJobService.startJob(dto);

        return buildJobResponse(response);
    }

    @GetMapping
    public ApiResponse<List<SortingJobDto>> getAllJobs() {

        List<SortingJobDto> response =
                sortingJobService.getAllJobs();

        return ApiResponse
                .<List<SortingJobDto>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SortingJobDto> getJobById(
            @PathVariable Long id
    ) {

        SortingJobDto response =
                sortingJobService.getJobById(id);

        return buildJobResponse(response);
    }

    @GetMapping("/{id}/status")
    public ApiResponse<SortingJobDto> getJobStatus(
            @PathVariable Long id
    ) {

        SortingJobDto response =
                sortingJobService.getJobStatus(id);

        return buildJobResponse(response);
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<SortingJobDto> cancelJob(
            @PathVariable Long id
    ) {

        SortingJobDto response =
                sortingJobService.cancelJob(id);

        return buildJobResponse(response);
    }

    private ApiResponse<SortingJobDto> buildJobResponse(
            SortingJobDto response
    ) {

        if (response.getErrorMessage() == null
                || response.getErrorMessage().trim().isEmpty()) {

            return ApiResponse
                    .<SortingJobDto>builder()
                    .success(true)
                    .results(response)
                    .errorCount(0)
                    .errors(Collections.emptyList())
                    .build();
        }

        return ApiResponse
                .<SortingJobDto>builder()
                .success(false)
                .results(response)
                .errorCount(1)
                .errors(
                        List.of(
                                response.getErrorMessage()
                        )
                )
                .build();
    }
}