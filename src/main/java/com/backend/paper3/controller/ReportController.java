package com.backend.paper3.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.dto.ReportSummaryDto;
import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/dataset/{datasetId}/summary")
    public ApiResponse<ReportSummaryDto> getDatasetReport(
            @PathVariable Long datasetId
    ) {

        ReportSummaryDto response =
                reportService.getDatasetReport(
                        datasetId
                );

        return ApiResponse
                .<ReportSummaryDto>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }
}