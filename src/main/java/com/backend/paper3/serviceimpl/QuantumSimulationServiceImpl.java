package com.backend.paper3.serviceimpl;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.PythonQuantumRequestDto;
import com.backend.paper3.entity.DatasetEntity;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.repository.DatasetRepository;
import com.backend.paper3.service.PythonQuantumClient;
import com.backend.paper3.service.QuantumSimulationService;

@Service
public class QuantumSimulationServiceImpl implements QuantumSimulationService {

    private static final int DEFAULT_SAMPLE_SIZE = 100000;

    private static final double DEFAULT_LEARNING_RATE = 0.1;

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private PythonQuantumClient pythonQuantumClient;

    @Override
    public Map<String, Object> simulateAmplitudeByDataset(Long datasetId) {

        DatasetEntity dataset = getDataset(datasetId);

        PythonQuantumRequestDto request =
                buildQuantumRequest(dataset, 10);

        Map<String, Object> pythonResponse =
                pythonQuantumClient.simulateAmplitude(request);

        return addDatasetContext(dataset, pythonResponse);
    }

    @Override
    public Map<String, Object> simulateInterferenceByDataset(Long datasetId) {

        DatasetEntity dataset = getDataset(datasetId);

        PythonQuantumRequestDto request =
                buildQuantumRequest(dataset, 10);

        request.setReinforcementStrength(1.25);
        request.setSuppressionStrength(0.75);

        Map<String, Object> pythonResponse =
                pythonQuantumClient.simulateInterference(request);

        return addDatasetContext(dataset, pythonResponse);
    }

    @Override
    public Map<String, Object> generateQasmByDataset(Long datasetId) {

        DatasetEntity dataset = getDataset(datasetId);

        PythonQuantumRequestDto request =
                buildQuantumRequest(dataset, 5);

        Map<String, Object> pythonResponse =
                pythonQuantumClient.generateQasm(request);

        return addDatasetContext(dataset, pythonResponse);
    }

    private DatasetEntity getDataset(Long datasetId) {

        if (datasetId == null) {
            throw new ApiException("Dataset id is required");
        }

        DatasetEntity dataset =
                datasetRepository
                        .findById(datasetId)
                        .orElseThrow(
                                () -> new ApiException(
                                        "Dataset not found with id : " + datasetId
                                )
                        );

        validateDataset(dataset);

        return dataset;
    }

    private void validateDataset(DatasetEntity dataset) {

        if (dataset.getFilePath() == null
                || dataset.getFilePath().trim().isEmpty()) {

            throw new ApiException("Dataset file path is missing");
        }

        if (dataset.getSelectedSortColumn() == null
                || dataset.getSelectedSortColumn().trim().isEmpty()) {

            throw new ApiException("Selected sort column is missing");
        }
    }

    private PythonQuantumRequestDto buildQuantumRequest(
            DatasetEntity dataset,
            Integer candidateCount
    ) {

        PythonQuantumRequestDto request =
                new PythonQuantumRequestDto();

        request.setFilePath(
                resolveAbsoluteFilePath(dataset.getFilePath())
        );

        request.setSelectedColumn(
                dataset.getSelectedSortColumn()
        );

        request.setSampleSize(
                DEFAULT_SAMPLE_SIZE
        );

        request.setCandidateCount(
                candidateCount
        );

        request.setLearningRate(
                DEFAULT_LEARNING_RATE
        );

        return request;
    }

    private String resolveAbsoluteFilePath(String filePath) {

        return Path
                .of(filePath)
                .toAbsolutePath()
                .toString();
    }

    private Map<String, Object> addDatasetContext(
            DatasetEntity dataset,
            Map<String, Object> pythonResponse
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("datasetId", dataset.getId());
        response.put("datasetName", dataset.getDatasetName());
        response.put("datasetUniqueId", dataset.getDatasetUniqueId());
        response.put("detectedPattern", dataset.getDetectedPattern());
        response.put("selectedSortColumn", dataset.getSelectedSortColumn());
        response.put("pythonQuantumResult", pythonResponse);

        return response;
    }
}