package com.backend.paper3.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.dto.DashboardSummaryDto;
import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryDto> getDashboardSummary() {

        DashboardSummaryDto response =
                dashboardService.getDashboardSummary();

        return ApiResponse
                .<DashboardSummaryDto>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }
}