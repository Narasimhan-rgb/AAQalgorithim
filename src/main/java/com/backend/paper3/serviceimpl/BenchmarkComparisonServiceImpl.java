package com.backend.paper3.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.AlgorithmComparisonDto;
import com.backend.paper3.dto.BenchmarkComparisonSummaryDto;
import com.backend.paper3.dto.BenchmarkResultDto;
import com.backend.paper3.entity.BenchmarkResultEntity;
import com.backend.paper3.enums.SortingAlgorithmType;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.repository.BenchmarkResultRepository;
import com.backend.paper3.service.BenchmarkComparisonService;

@Service
public class BenchmarkComparisonServiceImpl
        implements BenchmarkComparisonService {

    @Autowired
    private BenchmarkResultRepository benchmarkResultRepository;

    @Override
    public BenchmarkComparisonSummaryDto compareAlgorithmsByDataset(
            Long datasetId
    ) {

        if (datasetId == null) {
            throw new ApiException(
                    "Dataset id is required"
            );
        }

        List<BenchmarkResultEntity> results =
                benchmarkResultRepository
                        .findByDatasetIdOrderByCreatedAtDesc(
                                datasetId
                        );

        List<BenchmarkResultEntity> validResults =
                getValidResults(
                        results
                );

        if (validResults.isEmpty()) {
            return null; // Graceful empty state instead of 400 Bad Request
        }

        BenchmarkResultEntity bestAaqResult =
                findBestAaqResult(
                        validResults
                );

        if (bestAaqResult == null) {
            return null; // Return null if AAQ hasn't run yet, instead of erroring
        }

        BenchmarkResultEntity bestOverallResult =
                findBestOverallResult(
                        validResults
                );

        List<BenchmarkResultEntity> bestPerAlgorithm =
                getBestResultPerAlgorithm(
                        validResults
                );

        List<AlgorithmComparisonDto> comparisons =
                new ArrayList<>();

        for (BenchmarkResultEntity result : bestPerAlgorithm) {

            comparisons.add(
                    buildComparisonDto(
                            result,
                            bestAaqResult
                    )
            );
        }

        comparisons.sort(
                Comparator.comparing(
                        AlgorithmComparisonDto::getExecutionTimeMs
                )
        );

        BenchmarkComparisonSummaryDto summary =
                new BenchmarkComparisonSummaryDto();

        summary.setDatasetId(
                bestOverallResult.getDatasetId()
        );

        summary.setDatasetName(
                bestOverallResult.getDatasetName()
        );

        summary.setDatasetUniqueId(
                bestOverallResult.getDatasetUniqueId()
        );

        summary.setBestAlgorithm(
                bestOverallResult.getAlgorithm()
        );

        summary.setBestExecutionTimeMs(
                bestOverallResult.getExecutionTimeMs()
        );

        summary.setAaqAlgorithm(
                bestAaqResult.getAlgorithm()
        );

        summary.setAaqExecutionTimeMs(
                bestAaqResult.getExecutionTimeMs()
        );

        summary.setAaqThroughputRecordsPerSecond(
                safeDouble(
                        bestAaqResult.getThroughputRecordsPerSecond()
                )
        );

        summary.setTotalAlgorithmsCompared(
                (long) comparisons.size()
        );

        summary.setComparisons(
                comparisons
        );

        summary.setGeneratedAt(
                LocalDateTime.now()
        );

        return summary;
    }

    @Override
    public BenchmarkResultDto getBestResultByDataset(
            Long datasetId
    ) {

        if (datasetId == null) {
            throw new ApiException(
                    "Dataset id is required"
            );
        }

        List<BenchmarkResultEntity> results =
                benchmarkResultRepository
                        .findByDatasetIdOrderByCreatedAtDesc(
                                datasetId
                        );

        List<BenchmarkResultEntity> validResults =
                getValidResults(
                        results
                );

        if (validResults.isEmpty()) {
            return null; // Graceful empty state instead of 400 Bad Request
        }

        BenchmarkResultEntity bestResult =
                findBestOverallResult(
                        validResults
                );

        return mapToDto(
                bestResult
        );
    }

    private List<BenchmarkResultEntity> getValidResults(
            List<BenchmarkResultEntity> results
    ) {

        if (results == null) {
            return new ArrayList<>();
        }

        return results
                .stream()
                .filter(result -> result.getAlgorithm() != null)
                .filter(result -> result.getExecutionTimeMs() != null)
                .filter(result -> result.getExecutionTimeMs() > 0)
                .toList();
    }

    private BenchmarkResultEntity findBestAaqResult(
            List<BenchmarkResultEntity> results
    ) {

        return results
                .stream()
                .filter(result ->
                        result.getAlgorithm()
                                .equalsIgnoreCase(
                                        SortingAlgorithmType
                                                .ADAPTIVE_AMPLITUDE_QUICKSORT
                                                .name()
                                )
                )
                .min(
                        Comparator.comparing(
                                BenchmarkResultEntity::getExecutionTimeMs
                        )
                )
                .orElse(null);
    }

    private BenchmarkResultEntity findBestOverallResult(
            List<BenchmarkResultEntity> results
    ) {

        return results
                .stream()
                .min(
                        Comparator.comparing(
                                BenchmarkResultEntity::getExecutionTimeMs
                        )
                )
                .orElseThrow(
                        () -> new ApiException(
                                "No benchmark result available"
                        )
                );
    }

    private List<BenchmarkResultEntity> getBestResultPerAlgorithm(
            List<BenchmarkResultEntity> results
    ) {

        Map<String, BenchmarkResultEntity> bestMap =
                new LinkedHashMap<>();

        for (BenchmarkResultEntity result : results) {

            String algorithm =
                    result.getAlgorithm()
                            .trim()
                            .toUpperCase();

            BenchmarkResultEntity existing =
                    bestMap.get(
                            algorithm
                    );

            if (existing == null
                    || result.getExecutionTimeMs()
                    < existing.getExecutionTimeMs()) {

                bestMap.put(
                        algorithm,
                        result
                );
            }
        }

        return new ArrayList<>(
                bestMap.values()
        );
    }

    private AlgorithmComparisonDto buildComparisonDto(
            BenchmarkResultEntity result,
            BenchmarkResultEntity bestAaqResult
    ) {

        Long aaqTime =
                bestAaqResult.getExecutionTimeMs();

        Long currentTime =
                result.getExecutionTimeMs();

        AlgorithmComparisonDto dto =
                new AlgorithmComparisonDto();

        dto.setAlgorithm(
                result.getAlgorithm()
        );

        dto.setExecutionTimeMs(
                currentTime
        );

        dto.setInputSize(
                result.getInputSize()
        );

        dto.setComparisonCount(
                result.getComparisonCount()
        );

        dto.setSwapCount(
                result.getSwapCount()
        );

        dto.setThroughputRecordsPerSecond(
                safeDouble(
                        result.getThroughputRecordsPerSecond()
                )
        );

        dto.setImprovementPercentageVsAaq(
                calculateAaqImprovementPercentage(
                        aaqTime,
                        currentTime
                )
        );

        dto.setAaqFasterThanThisAlgorithm(
                aaqTime < currentTime
        );

        dto.setCreatedAt(
                result.getCreatedAt()
        );

        return dto;
    }

    private Double calculateAaqImprovementPercentage(
            Long aaqTimeMs,
            Long comparedTimeMs
    ) {

        if (aaqTimeMs == null
                || comparedTimeMs == null
                || comparedTimeMs <= 0) {

            return 0.0;
        }

        return ((comparedTimeMs - aaqTimeMs)
                / (double) comparedTimeMs) * 100.0;
    }

    private Double safeDouble(
            Double value
    ) {

        if (value == null
                || value.isNaN()
                || value.isInfinite()) {

            return 0.0;
        }

        return value;
    }

    private BenchmarkResultDto mapToDto(
            BenchmarkResultEntity entity
    ) {

        BenchmarkResultDto dto =
                new BenchmarkResultDto();

        dto.setId(
                entity.getId()
        );

        dto.setJobId(
                entity.getJobId()
        );

        dto.setJobUniqueId(
                entity.getJobUniqueId()
        );

        dto.setDatasetId(
                entity.getDatasetId()
        );

        dto.setDatasetName(
                entity.getDatasetName()
        );

        dto.setDatasetUniqueId(
                entity.getDatasetUniqueId()
        );

        dto.setAlgorithm(
                entity.getAlgorithm()
        );

        dto.setSelectedColumn(
                entity.getSelectedColumn()
        );

        dto.setInputSize(
                entity.getInputSize()
        );

        dto.setExecutionTimeMs(
                entity.getExecutionTimeMs()
        );

        dto.setComparisonCount(
                entity.getComparisonCount()
        );

        dto.setSwapCount(
                entity.getSwapCount()
        );

        dto.setThroughputRecordsPerSecond(
                entity.getThroughputRecordsPerSecond()
        );

        dto.setStatus(
                entity.getStatus()
        );

        dto.setBenchmarkExecutionTimeMs(
                entity.getBenchmarkExecutionTimeMs()
        );

        dto.setBenchmarkMemoryUsageMb(
                entity.getBenchmarkMemoryUsageMb()
        );

        dto.setBenchmarkCpuUsage(
                entity.getBenchmarkCpuUsage()
        );

        dto.setBenchmarkThroughput(
                entity.getBenchmarkThroughput()
        );

        dto.setImprovementPercentage(
                entity.getImprovementPercentage()
        );

        dto.setCreatedAt(
                entity.getCreatedAt()
        );

        return dto;
    }
}