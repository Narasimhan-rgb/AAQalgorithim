package com.backend.paper3.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.AlgorithmRecommendationDto;
import com.backend.paper3.dto.BenchmarkComparisonSummaryDto;
import com.backend.paper3.dto.BenchmarkResultDto;
import com.backend.paper3.dto.QuantumAaqMetricsDto;
import com.backend.paper3.dto.ReportSummaryDto;
import com.backend.paper3.entity.AlgorithmRecommendationEntity;
import com.backend.paper3.entity.DatasetEntity;
import com.backend.paper3.entity.QuantumAaqMetricsEntity;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.repository.AlgorithmRecommendationRepository;
import com.backend.paper3.repository.DatasetRepository;
import com.backend.paper3.repository.QuantumAaqMetricsRepository;
import com.backend.paper3.service.BenchmarkComparisonService;
import com.backend.paper3.service.ReportService;

@Service
public class ReportServiceImpl
        implements ReportService {

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private AlgorithmRecommendationRepository algorithmRecommendationRepository;

    @Autowired
    private QuantumAaqMetricsRepository quantumAaqMetricsRepository;

    @Autowired
    private BenchmarkComparisonService benchmarkComparisonService;

    @Override
    public ReportSummaryDto getDatasetReport(
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

        ReportSummaryDto report =
                new ReportSummaryDto();

        mapDatasetDetails(
                report,
                dataset
        );

        report.setLatestRecommendation(
                getLatestRecommendation(
                        datasetId
                )
        );

        report.setLatestQuantumMetrics(
                getLatestQuantumMetrics(
                        datasetId
                )
        );

        try {

            BenchmarkComparisonSummaryDto comparison =
                    benchmarkComparisonService
                            .compareAlgorithmsByDataset(
                                    datasetId
                            );

            report.setBenchmarkComparison(
                    comparison
            );

        } catch (Exception e) {

            report.setBenchmarkComparison(
                    null
            );
        }

        try {

            BenchmarkResultDto bestResult =
                    benchmarkComparisonService
                            .getBestResultByDataset(
                                    datasetId
                            );

            report.setBestBenchmarkResult(
                    bestResult
            );

        } catch (Exception e) {

            report.setBestBenchmarkResult(
                    null
            );
        }

        setReportStatus(
                report
        );

        report.setGeneratedAt(
                LocalDateTime.now()
        );

        return report;
    }

    private void mapDatasetDetails(
            ReportSummaryDto report,
            DatasetEntity dataset
    ) {

        report.setDatasetId(
                dataset.getId()
        );

        report.setDatasetUniqueId(
                dataset.getDatasetUniqueId()
        );

        report.setDatasetName(
                dataset.getDatasetName()
        );

        report.setOriginalFileName(
                dataset.getOriginalFileName()
        );

        report.setFileType(
                dataset.getFileType()
        );

        report.setFileSizeBytes(
                dataset.getFileSizeBytes()
        );

        report.setRecordCount(
                dataset.getRecordCount()
        );

        report.setColumnCount(
                dataset.getColumnCount()
        );

        report.setSelectedSortColumn(
                dataset.getSelectedSortColumn()
        );

        report.setDataType(
                dataset.getDataType()
        );

        report.setDetectedPattern(
                dataset.getDetectedPattern()
        );

        report.setDuplicatePercentage(
                dataset.getDuplicatePercentage()
        );

        report.setNullPercentage(
                dataset.getNullPercentage()
        );

        report.setSkewnessValue(
                dataset.getSkewnessValue()
        );

        report.setSortednessScore(
                dataset.getSortednessScore()
        );

        report.setQuantumScore(
                dataset.getQuantumScore()
        );

        report.setFinalScore(
                dataset.getFinalScore()
        );
    }

    private AlgorithmRecommendationDto getLatestRecommendation(
            Long datasetId
    ) {

        List<AlgorithmRecommendationEntity> recommendations =
                algorithmRecommendationRepository
                        .findByDatasetIdOrderByCreatedAtDesc(
                                datasetId
                        );

        if (recommendations == null
                || recommendations.isEmpty()) {

            return null;
        }

        return mapRecommendationToDto(
                recommendations.get(0)
        );
    }

    private QuantumAaqMetricsDto getLatestQuantumMetrics(
            Long datasetId
    ) {

        List<QuantumAaqMetricsEntity> metrics =
                quantumAaqMetricsRepository
                        .findByDatasetIdOrderByCreatedAtDesc(
                                datasetId
                        );

        if (metrics == null
                || metrics.isEmpty()) {

            return null;
        }

        return mapQuantumMetricsToDto(
                metrics.get(0)
        );
    }

    private void setReportStatus(
            ReportSummaryDto report
    ) {

        boolean hasRecommendation =
                report.getLatestRecommendation() != null;

        boolean hasBenchmark =
                report.getBenchmarkComparison() != null;

        boolean hasQuantumMetrics =
                report.getLatestQuantumMetrics() != null;

        if (hasRecommendation
                && hasBenchmark
                && hasQuantumMetrics) {

            report.setReportStatus(
                    "READY"
            );

            report.setReportMessage(
                    "Dataset report is complete with recommendation, benchmark comparison, and AAQ quantum metrics."
            );

            return;
        }

        report.setReportStatus(
                "PARTIAL"
        );

        report.setReportMessage(
                "Dataset report is partially available. Run recommendation, sorting, and benchmark comparison for complete report."
        );
    }

    private AlgorithmRecommendationDto mapRecommendationToDto(
            AlgorithmRecommendationEntity entity
    ) {

        AlgorithmRecommendationDto dto =
                new AlgorithmRecommendationDto();

        dto.setId(
                entity.getId()
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

        dto.setDatasetPattern(
                entity.getDatasetPattern()
        );

        dto.setRecordCount(
                entity.getRecordCount()
        );

        dto.setDuplicatePercentage(
                entity.getDuplicatePercentage()
        );

        dto.setSkewnessValue(
                entity.getSkewnessValue()
        );

        dto.setSortednessScore(
                entity.getSortednessScore()
        );

        dto.setRecommendedAlgorithm(
                entity.getRecommendedAlgorithm()
        );

        dto.setConfidenceScore(
                entity.getConfidenceScore()
        );

        dto.setRecommendationReason(
                entity.getRecommendationReason()
        );

        dto.setCreatedAt(
                entity.getCreatedAt()
        );

        return dto;
    }

    private QuantumAaqMetricsDto mapQuantumMetricsToDto(
            QuantumAaqMetricsEntity entity
    ) {

        QuantumAaqMetricsDto dto =
                new QuantumAaqMetricsDto();

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

        dto.setComparisonCount(
                entity.getComparisonCount()
        );

        dto.setSwapCount(
                entity.getSwapCount()
        );

        dto.setPivotSelectionCount(
                entity.getPivotSelectionCount()
        );

        dto.setInsertionSortUsageCount(
                entity.getInsertionSortUsageCount()
        );

        dto.setHeapFallbackCount(
                entity.getHeapFallbackCount()
        );

        dto.setPartitionCount(
                entity.getPartitionCount()
        );

        dto.setAveragePartitionImbalance(
                entity.getAveragePartitionImbalance()
        );

        dto.setMaxPartitionImbalance(
                entity.getMaxPartitionImbalance()
        );

        dto.setCreatedAt(
                entity.getCreatedAt()
        );

        return dto;
    }
}