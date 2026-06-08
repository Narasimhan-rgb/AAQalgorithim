package com.backend.paper3.serviceimpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.AlgorithmRecommendationDto;
import com.backend.paper3.dto.BenchmarkComparisonSummaryDto;
import com.backend.paper3.dto.BenchmarkResultDto;
import com.backend.paper3.dto.QuantumAaqMetricsDto;
import com.backend.paper3.dto.ReportGeneratedDto;
import com.backend.paper3.dto.ReportSummaryDto;
import com.backend.paper3.entity.AlgorithmRecommendationEntity;
import com.backend.paper3.entity.DatasetEntity;
import com.backend.paper3.entity.QuantumAaqMetricsEntity;
import com.backend.paper3.entity.ReportEntity;
import com.backend.paper3.entity.SortingJobEntity;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.repository.AlgorithmRecommendationRepository;
import com.backend.paper3.repository.DatasetRepository;
import com.backend.paper3.repository.QuantumAaqMetricsRepository;
import com.backend.paper3.repository.ReportRepository;
import com.backend.paper3.repository.SortingJobRepository;
import com.backend.paper3.service.BenchmarkComparisonService;
import com.backend.paper3.service.QuantumSimulationService;
import com.backend.paper3.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private AlgorithmRecommendationRepository algorithmRecommendationRepository;

    @Autowired
    private QuantumAaqMetricsRepository quantumAaqMetricsRepository;

    @Autowired
    private BenchmarkComparisonService benchmarkComparisonService;

    @Autowired
    private SortingJobRepository sortingJobRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private QuantumSimulationService quantumSimulationService;

    @Override
    public ReportSummaryDto getDatasetReport(Long datasetId) {

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

        ReportSummaryDto report =
                new ReportSummaryDto();

        mapDatasetDetails(
                report,
                dataset
        );

        report.setLatestRecommendation(
                getLatestRecommendation(datasetId)
        );

        report.setLatestQuantumMetrics(
                getLatestQuantumMetrics(datasetId)
        );

        setPythonQuantumSimulationResults(
                report,
                datasetId
        );

        try {

            BenchmarkComparisonSummaryDto comparison =
                    benchmarkComparisonService
                            .compareAlgorithmsByDataset(datasetId);

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
                            .getBestResultByDataset(datasetId);

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

    @Override
    public ReportGeneratedDto generateReportForJob(Long jobId) {

        if (jobId == null) {
            throw new ApiException("Job id is required");
        }

        SortingJobEntity job =
                sortingJobRepository
                        .findById(jobId)
                        .orElseThrow(
                                () -> new ApiException(
                                        "Sorting job not found with id : " + jobId
                                )
                        );

        if (job.getDatasetId() == null) {
            throw new ApiException(
                    "Dataset id not found for job id : " + jobId
            );
        }

        ReportSummaryDto summary =
                getDatasetReport(
                        job.getDatasetId()
                );

        String reportName =
                "AAQ_REPORT_JOB_" + job.getId();

        String reportType =
                "DATASET_SUMMARY";

        String reportText =
                buildReportText(
                        summary,
                        job
                );

        String reportFilePath =
                saveReportFile(
                        reportName,
                        reportText
                );

        ReportEntity entity =
                new ReportEntity();

        entity.setJobId(
                job.getId()
        );

        entity.setDatasetId(
                job.getDatasetId()
        );

        entity.setReportName(
                reportName
        );

        entity.setReportType(
                reportType
        );

        entity.setReportFilePath(
                reportFilePath
        );

        entity.setCreatedBy(
                null
        );

        entity.setCreatedAt(
                LocalDateTime.now()
        );

        ReportEntity savedEntity =
                reportRepository.save(
                        entity
                );

        return mapReportGeneratedToDto(
                savedEntity
        );
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

    private AlgorithmRecommendationDto getLatestRecommendation(Long datasetId) {

        List<AlgorithmRecommendationEntity> recommendations =
                algorithmRecommendationRepository
                        .findByDatasetIdOrderByCreatedAtDesc(datasetId);

        if (recommendations == null || recommendations.isEmpty()) {
            return null;
        }

        return mapRecommendationToDto(
                recommendations.get(0)
        );
    }

    private QuantumAaqMetricsDto getLatestQuantumMetrics(Long datasetId) {

        List<QuantumAaqMetricsEntity> metrics =
                quantumAaqMetricsRepository
                        .findByDatasetIdOrderByCreatedAtDesc(datasetId);

        if (metrics == null || metrics.isEmpty()) {
            return null;
        }

        return mapQuantumMetricsToDto(
                metrics.get(0)
        );
    }

    private void setPythonQuantumSimulationResults(
            ReportSummaryDto report,
            Long datasetId
    ) {

        try {

            Map<String, Object> amplitudeResult =
                    quantumSimulationService
                            .simulateAmplitudeByDataset(datasetId);

            report.setQuantumAmplitudeSimulation(
                    amplitudeResult
            );

        } catch (Exception e) {

            report.setQuantumAmplitudeSimulation(
                    null
            );
        }

        try {

            Map<String, Object> interferenceResult =
                    quantumSimulationService
                            .simulateInterferenceByDataset(datasetId);

            report.setQuantumInterferenceSimulation(
                    interferenceResult
            );

        } catch (Exception e) {

            report.setQuantumInterferenceSimulation(
                    null
            );
        }

        try {

            Map<String, Object> qasmResult =
                    quantumSimulationService
                            .generateQasmByDataset(datasetId);

            report.setQuantumQasmSimulation(
                    qasmResult
            );

        } catch (Exception e) {

            report.setQuantumQasmSimulation(
                    null
            );
        }
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

        boolean hasPythonQuantumSimulation =
                report.getQuantumAmplitudeSimulation() != null
                        || report.getQuantumInterferenceSimulation() != null
                        || report.getQuantumQasmSimulation() != null;

        if (hasRecommendation
                && hasBenchmark
                && hasQuantumMetrics
                && hasPythonQuantumSimulation) {

            report.setReportStatus(
                    "READY"
            );

            report.setReportMessage(
                    "Dataset report is complete with recommendation, benchmark comparison, AAQ metrics, and Python quantum simulation results."
            );

            return;
        }

        if (hasRecommendation
                && hasBenchmark
                && hasQuantumMetrics) {

            report.setReportStatus(
                    "READY"
            );

            report.setReportMessage(
                    "Dataset report is complete with recommendation, benchmark comparison, and AAQ metrics. Python quantum simulation is not available."
            );

            return;
        }

        report.setReportStatus(
                "PARTIAL"
        );

        report.setReportMessage(
                "Dataset report is partially available. Run recommendation, sorting, benchmark, and quantum simulation for complete report."
        );
    }

    private String saveReportFile(
            String reportName,
            String reportText
    ) {

        try {

            Path reportsDirectory =
                    Path.of(
                            "reports",
                            "generated"
                    );

            Files.createDirectories(
                    reportsDirectory
            );

            Path reportFile =
                    reportsDirectory.resolve(
                            reportName + ".txt"
                    );

            Files.writeString(
                    reportFile,
                    reportText,
                    StandardCharsets.UTF_8
            );

            return reportFile
                    .toAbsolutePath()
                    .toString();

        } catch (IOException e) {

            throw new ApiException(
                    "Failed to save report file : " + e.getMessage()
            );
        }
    }

    private String buildReportText(
            ReportSummaryDto summary,
            SortingJobEntity job
    ) {

        StringBuilder builder =
                new StringBuilder();

        builder.append("AAQ Dataset Report\n");
        builder.append("==================\n\n");

        builder.append("Job ID: ")
                .append(job.getId())
                .append("\n");

        builder.append("Job Unique ID: ")
                .append(job.getJobUniqueId())
                .append("\n");

        builder.append("Requested Algorithm: ")
                .append(job.getRequestedAlgorithm())
                .append("\n");

        builder.append("Selected Column: ")
                .append(job.getSelectedColumn())
                .append("\n");

        builder.append("Job Status: ")
                .append(job.getStatus())
                .append("\n\n");

        builder.append("Dataset Details\n");
        builder.append("---------------\n");

        builder.append("Dataset ID: ")
                .append(summary.getDatasetId())
                .append("\n");

        builder.append("Dataset Name: ")
                .append(summary.getDatasetName())
                .append("\n");

        builder.append("Dataset Unique ID: ")
                .append(summary.getDatasetUniqueId())
                .append("\n");

        builder.append("Original File Name: ")
                .append(summary.getOriginalFileName())
                .append("\n");

        builder.append("File Type: ")
                .append(summary.getFileType())
                .append("\n");

        builder.append("Record Count: ")
                .append(summary.getRecordCount())
                .append("\n");

        builder.append("Column Count: ")
                .append(summary.getColumnCount())
                .append("\n");

        builder.append("Detected Pattern: ")
                .append(summary.getDetectedPattern())
                .append("\n");

        builder.append("Duplicate Percentage: ")
                .append(summary.getDuplicatePercentage())
                .append("\n");

        builder.append("Null Percentage: ")
                .append(summary.getNullPercentage())
                .append("\n");

        builder.append("Skewness Value: ")
                .append(summary.getSkewnessValue())
                .append("\n");

        builder.append("Sortedness Score: ")
                .append(summary.getSortednessScore())
                .append("\n\n");

        builder.append("Recommendation\n");
        builder.append("--------------\n");

        if (summary.getLatestRecommendation() != null) {

            builder.append("Recommended Algorithm: ")
                    .append(summary.getLatestRecommendation()
                            .getRecommendedAlgorithm())
                    .append("\n");

            builder.append("Confidence Score: ")
                    .append(summary.getLatestRecommendation()
                            .getConfidenceScore())
                    .append("\n");

            builder.append("Reason: ")
                    .append(summary.getLatestRecommendation()
                            .getRecommendationReason())
                    .append("\n\n");

        } else {

            builder.append("No recommendation available.\n\n");
        }

        builder.append("Benchmark Result\n");
        builder.append("----------------\n");

        if (summary.getBenchmarkComparison() != null) {

            builder.append("Best Algorithm: ")
                    .append(summary.getBenchmarkComparison()
                            .getBestAlgorithm())
                    .append("\n");

            builder.append("Best Execution Time: ")
                    .append(summary.getBenchmarkComparison()
                            .getBestExecutionTimeMs())
                    .append(" ms\n");

            builder.append("AAQ Algorithm: ")
                    .append(summary.getBenchmarkComparison()
                            .getAaqAlgorithm())
                    .append("\n");

            builder.append("AAQ Execution Time: ")
                    .append(summary.getBenchmarkComparison()
                            .getAaqExecutionTimeMs())
                    .append(" ms\n");

            builder.append("AAQ Throughput: ")
                    .append(summary.getBenchmarkComparison()
                            .getAaqThroughputRecordsPerSecond())
                    .append(" records/sec\n\n");

        } else {

            builder.append("No benchmark comparison available.\n\n");
        }

        builder.append("Quantum AAQ Metrics\n");
        builder.append("-------------------\n");

        if (summary.getLatestQuantumMetrics() != null) {

            builder.append("Pivot Selection Count: ")
                    .append(summary.getLatestQuantumMetrics()
                            .getPivotSelectionCount())
                    .append("\n");

            builder.append("Insertion Sort Usage Count: ")
                    .append(summary.getLatestQuantumMetrics()
                            .getInsertionSortUsageCount())
                    .append("\n");

            builder.append("Heap Fallback Count: ")
                    .append(summary.getLatestQuantumMetrics()
                            .getHeapFallbackCount())
                    .append("\n");

            builder.append("Partition Count: ")
                    .append(summary.getLatestQuantumMetrics()
                            .getPartitionCount())
                    .append("\n");

            builder.append("Average Partition Imbalance: ")
                    .append(summary.getLatestQuantumMetrics()
                            .getAveragePartitionImbalance())
                    .append("\n");

            builder.append("Max Partition Imbalance: ")
                    .append(summary.getLatestQuantumMetrics()
                            .getMaxPartitionImbalance())
                    .append("\n\n");

        } else {

            builder.append("No AAQ quantum metrics available.\n\n");
        }

        appendPythonQuantumSimulationSection(
                builder,
                summary
        );

        builder.append("Report Status: ")
                .append(summary.getReportStatus())
                .append("\n");

        builder.append("Report Message: ")
                .append(summary.getReportMessage())
                .append("\n");

        builder.append("Generated At: ")
                .append(LocalDateTime.now())
                .append("\n");

        return builder.toString();
    }

    private void appendPythonQuantumSimulationSection(
            StringBuilder builder,
            ReportSummaryDto summary
    ) {

        builder.append("Python Quantum Simulation\n");
        builder.append("-------------------------\n");

        Map<String, Object> amplitude =
                extractPythonQuantumResult(
                        summary.getQuantumAmplitudeSimulation()
                );

        Map<String, Object> interference =
                extractPythonQuantumResult(
                        summary.getQuantumInterferenceSimulation()
                );

        Map<String, Object> qasm =
                extractPythonQuantumResult(
                        summary.getQuantumQasmSimulation()
                );

        if (amplitude != null) {

            builder.append("Amplitude Selected Pivot: ")
                    .append(getValue(amplitude, "selectedPivotValue"))
                    .append("\n");

            builder.append("Amplitude Best Partition Imbalance: ")
                    .append(getValue(amplitude, "bestPartitionImbalance"))
                    .append("\n");

            builder.append("Amplitude Convergence Score: ")
                    .append(getValue(amplitude, "amplitudeConvergenceScore"))
                    .append("\n");

        } else {

            builder.append("Amplitude Simulation: Not available\n");
        }

        if (interference != null) {

            builder.append("Interference Gain: ")
                    .append(getValue(interference, "interferenceGain"))
                    .append("\n");

            builder.append("Constructive Reinforcement Count: ")
                    .append(getValue(interference, "constructiveReinforcementCount"))
                    .append("\n");

            builder.append("Destructive Suppression Count: ")
                    .append(getValue(interference, "destructiveSuppressionCount"))
                    .append("\n");

        } else {

            builder.append("Interference Simulation: Not available\n");
        }

        if (qasm != null) {

            builder.append("OpenQASM Qubit Count: ")
                    .append(getValue(qasm, "qubitCount"))
                    .append("\n");

            builder.append("OpenQASM Selected Pivot: ")
                    .append(getValue(qasm, "selectedPivotValue"))
                    .append("\n");

        } else {

            builder.append("OpenQASM Simulation: Not available\n");
        }

        builder.append("\n");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractPythonQuantumResult(
            Map<String, Object> response
    ) {

        if (response == null) {
            return null;
        }

        Object result =
                response.get("pythonQuantumResult");

        if (result instanceof Map) {
            return (Map<String, Object>) result;
        }

        return response;
    }

    private Object getValue(
            Map<String, Object> map,
            String key
    ) {

        if (map == null) {
            return null;
        }

        return map.get(key);
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

    private ReportGeneratedDto mapReportGeneratedToDto(
            ReportEntity entity
    ) {

        ReportGeneratedDto dto =
                new ReportGeneratedDto();

        dto.setId(
                entity.getId()
        );

        dto.setJobId(
                entity.getJobId()
        );

        dto.setDatasetId(
                entity.getDatasetId()
        );

        dto.setReportName(
                entity.getReportName()
        );

        dto.setReportType(
                entity.getReportType()
        );

        dto.setReportFilePath(
                entity.getReportFilePath()
        );

        dto.setCreatedBy(
                entity.getCreatedBy()
        );

        dto.setCreatedAt(
                entity.getCreatedAt()
        );

        return dto;
    }
}