package com.backend.paper3.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.algorithm.AAQMetrics;
import com.backend.paper3.dto.QuantumAaqMetricsDto;
import com.backend.paper3.dto.SortingRunResultDto;
import com.backend.paper3.entity.QuantumAaqMetricsEntity;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.repository.QuantumAaqMetricsRepository;
import com.backend.paper3.service.QuantumAaqMetricsService;

@Service
public class QuantumAaqMetricsServiceImpl
        implements QuantumAaqMetricsService {

    @Autowired
    private QuantumAaqMetricsRepository quantumAaqMetricsRepository;

    @Override
    public QuantumAaqMetricsDto saveAaqMetrics(
            SortingRunResultDto sortingResult,
            AAQMetrics metrics
    ) {

        if (sortingResult == null) {
            throw new ApiException("Sorting result is required");
        }

        if (metrics == null) {
            throw new ApiException("AAQ metrics are required");
        }

        QuantumAaqMetricsEntity entity =
                new QuantumAaqMetricsEntity();

        entity.setJobId(
                sortingResult.getJobId()
        );

        entity.setJobUniqueId(
                sortingResult.getJobUniqueId()
        );

        entity.setDatasetId(
                sortingResult.getDatasetId()
        );

        entity.setDatasetName(
                sortingResult.getDatasetName()
        );

        entity.setDatasetUniqueId(
                sortingResult.getDatasetUniqueId()
        );

        entity.setAlgorithm(
                sortingResult.getAlgorithm()
        );

        entity.setComparisonCount(
                metrics.getComparisonCount()
        );

        entity.setSwapCount(
                metrics.getSwapCount()
        );

        entity.setPivotSelectionCount(
                metrics.getPivotSelectionCount()
        );

        entity.setInsertionSortUsageCount(
                metrics.getInsertionSortUsageCount()
        );

        entity.setHeapFallbackCount(
                metrics.getHeapFallbackCount()
        );

        entity.setPartitionCount(
                metrics.getPartitionCount()
        );

        entity.setAveragePartitionImbalance(
                metrics.getAveragePartitionImbalance()
        );

        entity.setMaxPartitionImbalance(
                metrics.getMaxPartitionImbalance()
        );

        entity.setCreatedAt(
                LocalDateTime.now()
        );

        QuantumAaqMetricsEntity savedEntity =
                quantumAaqMetricsRepository.save(entity);

        return mapToDto(savedEntity);
    }

    @Override
    public List<QuantumAaqMetricsDto> getMetricsByJobId(
            Long jobId
    ) {

        if (jobId == null) {
            throw new ApiException("Job id is required");
        }

        return quantumAaqMetricsRepository
                .findByJobIdOrderByCreatedAtDesc(jobId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuantumAaqMetricsDto> getMetricsByDatasetId(
            Long datasetId
    ) {

        if (datasetId == null) {
            throw new ApiException("Dataset id is required");
        }

        return quantumAaqMetricsRepository
                .findByDatasetIdOrderByCreatedAtDesc(datasetId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private QuantumAaqMetricsDto mapToDto(
            QuantumAaqMetricsEntity entity
    ) {

        QuantumAaqMetricsDto dto =
                new QuantumAaqMetricsDto();

        dto.setId(entity.getId());
        dto.setJobId(entity.getJobId());
        dto.setJobUniqueId(entity.getJobUniqueId());
        dto.setDatasetId(entity.getDatasetId());
        dto.setDatasetName(entity.getDatasetName());
        dto.setDatasetUniqueId(entity.getDatasetUniqueId());
        dto.setAlgorithm(entity.getAlgorithm());
        dto.setComparisonCount(entity.getComparisonCount());
        dto.setSwapCount(entity.getSwapCount());
        dto.setPivotSelectionCount(entity.getPivotSelectionCount());
        dto.setInsertionSortUsageCount(entity.getInsertionSortUsageCount());
        dto.setHeapFallbackCount(entity.getHeapFallbackCount());
        dto.setPartitionCount(entity.getPartitionCount());
        dto.setAveragePartitionImbalance(entity.getAveragePartitionImbalance());
        dto.setMaxPartitionImbalance(entity.getMaxPartitionImbalance());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}