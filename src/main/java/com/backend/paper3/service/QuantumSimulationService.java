package com.backend.paper3.service;

import java.util.Map;

public interface QuantumSimulationService {

    Map<String, Object> simulateAmplitudeByDataset(Long datasetId);

    Map<String, Object> simulateInterferenceByDataset(Long datasetId);

    Map<String, Object> generateQasmByDataset(Long datasetId);
}