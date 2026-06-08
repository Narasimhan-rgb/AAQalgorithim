package com.backend.paper3.serviceimpl;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.PythonProfileRequestDto;
import com.backend.paper3.dto.PythonProfileResponseDto;
import com.backend.paper3.entity.DatasetEntity;
import com.backend.paper3.enums.DatasetPattern;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.repository.DatasetRepository;
import com.backend.paper3.service.DatasetAnalyzerService;
import com.backend.paper3.service.PythonProfilerClient;

@Service
public class DatasetAnalyzerServiceImpl
        implements DatasetAnalyzerService {

    private static final int DEFAULT_SAMPLE_SIZE =
            100000;

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private PythonProfilerClient pythonProfilerClient;

    @Override
    public PythonProfileResponseDto analyzeDataset(
            Long datasetId
    ) {

        if (datasetId == null) {
            throw new ApiException(
                    "Dataset id is required"
            );
        }

        DatasetEntity dataset =
                datasetRepository
                        .findById(datasetId)
                        .orElseThrow(
                                () -> new ApiException(
                                        "Dataset not found with id : "
                                                + datasetId
                                )
                        );

        validateDatasetForAnalysis(
                dataset
        );

        PythonProfileRequestDto request =
                new PythonProfileRequestDto();

        request.setFilePath(
                resolveAbsoluteFilePath(
                        dataset.getFilePath()
                )
        );

        request.setSelectedColumn(
                dataset.getSelectedSortColumn()
        );

        request.setSampleSize(
                DEFAULT_SAMPLE_SIZE
        );

        PythonProfileResponseDto profile =
                pythonProfilerClient.profileDataset(
                        request
                );

        updateDatasetProfile(
                dataset,
                profile
        );

        datasetRepository.save(
                dataset
        );

        return profile;
    }

    private void validateDatasetForAnalysis(
            DatasetEntity dataset
    ) {

        if (dataset.getFilePath() == null
                || dataset.getFilePath().trim().isEmpty()) {

            throw new ApiException(
                    "Dataset file path is missing"
            );
        }

        if (dataset.getSelectedSortColumn() == null
                || dataset.getSelectedSortColumn().trim().isEmpty()) {

            throw new ApiException(
                    "Selected sort column is missing"
            );
        }
    }

    private String resolveAbsoluteFilePath(
            String filePath
    ) {

        return Path
                .of(filePath)
                .toAbsolutePath()
                .toString();
    }

    private void updateDatasetProfile(
            DatasetEntity dataset,
            PythonProfileResponseDto profile
    ) {

        if (profile == null) {
            throw new ApiException(
                    "Python profile response is empty"
            );
        }

        dataset.setRecordCount(
                profile.getRowCount()
        );

        dataset.setColumnCount(
                profile.getColumnCount()
        );

        dataset.setSelectedSortColumn(
                profile.getSelectedColumn()
        );

        dataset.setDataType(
                profile.getDataType()
        );

        dataset.setNullPercentage(
                profile.getNullPercentage()
        );

        dataset.setDuplicatePercentage(
                profile.getDuplicatePercentage()
        );

        dataset.setSkewnessValue(
                profile.getSkewness()
        );

        dataset.setSortednessScore(
                profile.getSortednessScore()
        );

        dataset.setDetectedPattern(
                resolveDatasetPattern(
                        profile.getDetectedPattern()
                )
        );
    }

    private DatasetPattern resolveDatasetPattern(
            String detectedPattern
    ) {

        if (detectedPattern == null
                || detectedPattern.trim().isEmpty()) {

            return DatasetPattern.UNKNOWN;
        }

        try {

            return DatasetPattern.valueOf(
                    detectedPattern.trim().toUpperCase()
            );

        } catch (Exception e) {

            return DatasetPattern.UNKNOWN;
        }
    }
}