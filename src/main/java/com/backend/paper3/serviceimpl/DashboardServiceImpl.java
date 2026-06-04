package com.backend.paper3.serviceimpl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.DashboardSummaryDto;
import com.backend.paper3.entity.AlgorithmRecommendationEntity;
import com.backend.paper3.entity.BenchmarkResultEntity;
import com.backend.paper3.entity.QuantumAaqMetricsEntity;
import com.backend.paper3.entity.SortingJobEntity;
import com.backend.paper3.enums.SortingAlgorithmType;
import com.backend.paper3.enums.SortingJobStatus;
import com.backend.paper3.repository.AlgorithmRecommendationRepository;
import com.backend.paper3.repository.BenchmarkResultRepository;
import com.backend.paper3.repository.DatasetRepository;
import com.backend.paper3.repository.QuantumAaqMetricsRepository;
import com.backend.paper3.repository.SortingJobRepository;
import com.backend.paper3.service.DashboardService;

@Service
public class DashboardServiceImpl
        implements DashboardService {

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private SortingJobRepository sortingJobRepository;

    @Autowired
    private BenchmarkResultRepository benchmarkResultRepository;

    @Autowired
    private AlgorithmRecommendationRepository algorithmRecommendationRepository;

    @Autowired
    private QuantumAaqMetricsRepository quantumAaqMetricsRepository;

    @Override
    public DashboardSummaryDto getDashboardSummary() {

        List<SortingJobEntity> jobs =
                sortingJobRepository.findAll();

        List<BenchmarkResultEntity> benchmarkResults =
                benchmarkResultRepository.findAll();

        List<AlgorithmRecommendationEntity> recommendations =
                algorithmRecommendationRepository.findAll();

        List<QuantumAaqMetricsEntity> quantumMetrics =
                quantumAaqMetricsRepository.findAll();

        DashboardSummaryDto dto =
                new DashboardSummaryDto();

        dto.setTotalDatasets(
                datasetRepository.count()
        );

        dto.setTotalSortingJobs(
                (long) jobs.size()
        );

        dto.setPendingJobs(
                countJobsByStatus(
                        jobs,
                        SortingJobStatus.PENDING
                )
        );

        dto.setRunningJobs(
                countJobsByStatus(
                        jobs,
                        SortingJobStatus.RUNNING
                )
        );

        dto.setCompletedJobs(
                countJobsByStatus(
                        jobs,
                        SortingJobStatus.COMPLETED
                )
        );

        dto.setFailedJobs(
                countJobsByStatus(
                        jobs,
                        SortingJobStatus.FAILED
                )
        );

        dto.setCancelledJobs(
                countJobsByStatus(
                        jobs,
                        SortingJobStatus.CANCELLED
                )
        );

        dto.setTotalBenchmarkResults(
                (long) benchmarkResults.size()
        );

        dto.setTotalRecommendations(
                (long) recommendations.size()
        );

        dto.setTotalQuantumMetricRecords(
                (long) quantumMetrics.size()
        );

        dto.setBestAaqExecutionTimeMs(
                calculateBestAaqExecutionTime(
                        benchmarkResults
                )
        );

        dto.setAverageThroughputRecordsPerSecond(
                calculateAverageThroughput(
                        benchmarkResults
                )
        );

        setLatestRecommendation(
                dto,
                recommendations
        );

        setLatestQuantumMetrics(
                dto,
                quantumMetrics
        );

        dto.setGeneratedAt(
                LocalDateTime.now()
        );

        return dto;
    }

    private Long countJobsByStatus(
            List<SortingJobEntity> jobs,
            SortingJobStatus status
    ) {

        return jobs
                .stream()
                .filter(job -> job.getStatus() == status)
                .count();
    }

    private Long calculateBestAaqExecutionTime(
            List<BenchmarkResultEntity> benchmarkResults
    ) {

        return benchmarkResults
                .stream()
                .filter(result ->
                        result.getAlgorithm() != null
                                && result.getAlgorithm().equalsIgnoreCase(
                                        SortingAlgorithmType
                                                .ADAPTIVE_AMPLITUDE_QUICKSORT
                                                .name()
                                )
                )
                .filter(result -> result.getExecutionTimeMs() != null)
                .map(BenchmarkResultEntity::getExecutionTimeMs)
                .min(Long::compareTo)
                .orElse(0L);
    }

    private Double calculateAverageThroughput(
            List<BenchmarkResultEntity> benchmarkResults
    ) {

        return benchmarkResults
                .stream()
                .map(BenchmarkResultEntity::getThroughputRecordsPerSecond)
                .filter(value -> value != null && value > 0.0)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private void setLatestRecommendation(
            DashboardSummaryDto dto,
            List<AlgorithmRecommendationEntity> recommendations
    ) {

        AlgorithmRecommendationEntity latestRecommendation =
                recommendations
                        .stream()
                        .filter(item -> item.getCreatedAt() != null)
                        .max(
                                Comparator.comparing(
                                        AlgorithmRecommendationEntity::getCreatedAt
                                )
                        )
                        .orElse(null);

        if (latestRecommendation == null) {
            dto.setLatestRecommendedAlgorithm(null);
            dto.setLatestRecommendationConfidence(0.0);
            dto.setLatestRecommendationReason(null);
            return;
        }

        dto.setLatestRecommendedAlgorithm(
                latestRecommendation.getRecommendedAlgorithm()
        );

        dto.setLatestRecommendationConfidence(
                latestRecommendation.getConfidenceScore()
        );

        dto.setLatestRecommendationReason(
                latestRecommendation.getRecommendationReason()
        );
    }

    private void setLatestQuantumMetrics(
            DashboardSummaryDto dto,
            List<QuantumAaqMetricsEntity> quantumMetrics
    ) {

        QuantumAaqMetricsEntity latestMetric =
                quantumMetrics
                        .stream()
                        .filter(item -> item.getCreatedAt() != null)
                        .max(
                                Comparator.comparing(
                                        QuantumAaqMetricsEntity::getCreatedAt
                                )
                        )
                        .orElse(null);

        if (latestMetric == null) {
            dto.setLatestQuantumJobId(null);
            dto.setLatestPivotSelectionCount(0L);
            dto.setLatestInsertionSortUsageCount(0L);
            dto.setLatestHeapFallbackCount(0L);
            dto.setLatestPartitionCount(0L);
            dto.setLatestAveragePartitionImbalance(0.0);
            dto.setLatestMaxPartitionImbalance(0.0);
            return;
        }

        dto.setLatestQuantumJobId(
                latestMetric.getJobId()
        );

        dto.setLatestPivotSelectionCount(
                safeLong(
                        latestMetric.getPivotSelectionCount()
                )
        );

        dto.setLatestInsertionSortUsageCount(
                safeLong(
                        latestMetric.getInsertionSortUsageCount()
                )
        );

        dto.setLatestHeapFallbackCount(
                safeLong(
                        latestMetric.getHeapFallbackCount()
                )
        );

        dto.setLatestPartitionCount(
                safeLong(
                        latestMetric.getPartitionCount()
                )
        );

        dto.setLatestAveragePartitionImbalance(
                safeDouble(
                        latestMetric.getAveragePartitionImbalance()
                )
        );

        dto.setLatestMaxPartitionImbalance(
                safeDouble(
                        latestMetric.getMaxPartitionImbalance()
                )
        );
    }

    private Long safeLong(
            Long value
    ) {

        return value == null
                ? 0L
                : value;
    }

    private Double safeDouble(
            Double value
    ) {

        return value == null
                ? 0.0
                : value;
    }
}