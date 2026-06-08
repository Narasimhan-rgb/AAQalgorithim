package com.backend.paper3.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.dto.SystemValidationDto;
import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.SystemValidationService;

@RestController
@RequestMapping("/system")
public class SystemValidationController {

    @Autowired
    private SystemValidationService systemValidationService;

    @GetMapping("/validate")
    public ApiResponse<SystemValidationDto> validateSystem() {

        SystemValidationDto response =
                systemValidationService.validateSystem();

        return ApiResponse
                .<SystemValidationDto>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }
}