package com.backend.paper3.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paper3.response.ApiResponse;
import com.backend.paper3.service.QuantumSimulationService;

@RestController
@RequestMapping("/quantum")
public class QuantumSimulationController {

    @Autowired
    private QuantumSimulationService quantumSimulationService;

    @PostMapping("/amplitude/dataset/{datasetId}")
    public ApiResponse<Map<String, Object>> simulateAmplitude(
            @PathVariable Long datasetId
    ) {

        Map<String, Object> response =
                quantumSimulationService.simulateAmplitudeByDataset(datasetId);

        return ApiResponse
                .<Map<String, Object>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @PostMapping("/interference/dataset/{datasetId}")
    public ApiResponse<Map<String, Object>> simulateInterference(
            @PathVariable Long datasetId
    ) {

        Map<String, Object> response =
                quantumSimulationService.simulateInterferenceByDataset(datasetId);

        return ApiResponse
                .<Map<String, Object>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }

    @PostMapping("/qasm/dataset/{datasetId}")
    public ApiResponse<Map<String, Object>> generateQasm(
            @PathVariable Long datasetId
    ) {

        Map<String, Object> response =
                quantumSimulationService.generateQasmByDataset(datasetId);

        return ApiResponse
                .<Map<String, Object>>builder()
                .success(true)
                .results(response)
                .errorCount(0)
                .errors(Collections.emptyList())
                .build();
    }
}