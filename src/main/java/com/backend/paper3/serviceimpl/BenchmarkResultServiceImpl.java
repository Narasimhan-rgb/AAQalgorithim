package com.backend.paper3.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.BenchmarkResultDto;
import com.backend.paper3.dto.SortingRunResultDto;
import com.backend.paper3.entity.BenchmarkResultEntity;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.repository.BenchmarkResultRepository;
import com.backend.paper3.service.BenchmarkResultService;

@Service
public class BenchmarkResultServiceImpl
        implements BenchmarkResultService {

    @Autowired
    private BenchmarkResultRepository benchmarkResultRepository;

    @Override
    public BenchmarkResultDto saveSortingRunResult(
            SortingRunResultDto result
    ) {

        if (result == null) {
            throw new ApiException("Sorting result is required");
        }

        BenchmarkResultEntity entity =
                new BenchmarkResultEntity();

        entity.setJobId(
                result.getJobId()
        );

        entity.setJobUniqueId(
                result.getJobUniqueId()
        );

        entity.setDatasetId(
                result.getDatasetId()
        );

        entity.setDatasetName(
                result.getDatasetName()
        );

        entity.setDatasetUniqueId(
                result.getDatasetUniqueId()
        );

        entity.setAlgorithm(
                result.getAlgorithm()
        );

        entity.setSelectedColumn(
                result.getSelectedColumn()
        );

        entity.setInputSize(
                result.getTotalValuesSorted()
        );

        entity.setExecutionTimeMs(
                result.getExecutionTimeMs()
        );

        entity.setComparisonCount(
                result.getComparisonCount()
        );

        entity.setSwapCount(
                result.getSwapCount()
        );

        entity.setThroughputRecordsPerSecond(
                calculateThroughput(
                        result.getTotalValuesSorted(),
                        result.getExecutionTimeMs()
                )
        );

        entity.setStatus(
                result.getStatus() == null
                        ? "UNKNOWN"
                        : result.getStatus().name()
        );

        entity.setCreatedAt(
                LocalDateTime.now()
        );

        BenchmarkResultEntity savedEntity =
                benchmarkResultRepository.save(entity);

        return mapToDto(savedEntity);
    }

    @Override
    public List<BenchmarkResultDto> getAllResults() {

        return benchmarkResultRepository
                .findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BenchmarkResultDto> getResultsByJobId(
            Long jobId
    ) {

        if (jobId == null) {
            throw new ApiException("Job id is required");
        }

        return benchmarkResultRepository
                .findByJobIdOrderByCreatedAtDesc(jobId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BenchmarkResultDto> getResultsByDatasetId(
            Long datasetId
    ) {

        if (datasetId == null) {
            throw new ApiException("Dataset id is required");
        }

        return benchmarkResultRepository
                .findByDatasetIdOrderByCreatedAtDesc(datasetId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Double calculateThroughput(
            Long inputSize,
            Long executionTimeMs
    ) {

        if (inputSize == null
                || executionTimeMs == null
                || executionTimeMs <= 0) {

            return 0.0;
        }

        return inputSize / (executionTimeMs / 1000.0);
    }

    private BenchmarkResultDto mapToDto(
            BenchmarkResultEntity entity
    ) {

        BenchmarkResultDto dto =
                new BenchmarkResultDto();

        dto.setId(entity.getId());
        dto.setJobId(entity.getJobId());
        dto.setJobUniqueId(entity.getJobUniqueId());
        dto.setDatasetId(entity.getDatasetId());
        dto.setDatasetName(entity.getDatasetName());
        dto.setDatasetUniqueId(entity.getDatasetUniqueId());
        dto.setAlgorithm(entity.getAlgorithm());
        dto.setSelectedColumn(entity.getSelectedColumn());
        dto.setInputSize(entity.getInputSize());
        dto.setExecutionTimeMs(entity.getExecutionTimeMs());
        dto.setComparisonCount(entity.getComparisonCount());
        dto.setSwapCount(entity.getSwapCount());
        dto.setThroughputRecordsPerSecond(entity.getThroughputRecordsPerSecond());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}