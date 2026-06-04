package com.backend.paper3.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.AlgorithmRecommendationDto;
import com.backend.paper3.entity.AlgorithmRecommendationEntity;
import com.backend.paper3.entity.DatasetEntity;
import com.backend.paper3.enums.DatasetPattern;
import com.backend.paper3.enums.SortingAlgorithmType;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.repository.AlgorithmRecommendationRepository;
import com.backend.paper3.repository.DatasetRepository;
import com.backend.paper3.service.AlgorithmRecommendationService;

@Service
public class AlgorithmRecommendationServiceImpl
        implements AlgorithmRecommendationService {

    @Autowired
    private AlgorithmRecommendationRepository algorithmRecommendationRepository;

    @Autowired
    private DatasetRepository datasetRepository;

    @Override
    public AlgorithmRecommendationDto recommendForDataset(
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

        RecommendationDecision decision =
                decideAlgorithm(dataset);

        AlgorithmRecommendationEntity entity =
                new AlgorithmRecommendationEntity();

        entity.setDatasetId(
                dataset.getId()
        );

        entity.setDatasetName(
                dataset.getDatasetName()
        );

        entity.setDatasetUniqueId(
                dataset.getDatasetUniqueId()
        );

        entity.setDatasetPattern(
                dataset.getDetectedPattern() == null
                        ? DatasetPattern.UNKNOWN.name()
                        : dataset.getDetectedPattern().name()
        );

        entity.setRecordCount(
                dataset.getRecordCount()
        );

        entity.setDuplicatePercentage(
                safeDouble(
                        dataset.getDuplicatePercentage()
                )
        );

        entity.setSkewnessValue(
                safeDouble(
                        dataset.getSkewnessValue()
                )
        );

        entity.setSortednessScore(
                normalizeSortednessScore(
                        dataset.getSortednessScore()
                )
        );

        entity.setRecommendedAlgorithm(
                decision.recommendedAlgorithm()
        );

        entity.setConfidenceScore(
                decision.confidenceScore()
        );

        entity.setRecommendationReason(
                decision.reason()
        );

        entity.setCreatedAt(
                LocalDateTime.now()
        );

        AlgorithmRecommendationEntity savedEntity =
                algorithmRecommendationRepository.save(entity);

        return mapToDto(savedEntity);
    }

    @Override
    public List<AlgorithmRecommendationDto> getAllRecommendations() {

        return algorithmRecommendationRepository
                .findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlgorithmRecommendationDto> getRecommendationsByDatasetId(
            Long datasetId
    ) {

        if (datasetId == null) {
            throw new ApiException(
                    "Dataset id is required"
            );
        }

        return algorithmRecommendationRepository
                .findByDatasetIdOrderByCreatedAtDesc(
                        datasetId
                )
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private RecommendationDecision decideAlgorithm(
            DatasetEntity dataset
    ) {

        DatasetPattern pattern =
                dataset.getDetectedPattern() == null
                        ? DatasetPattern.UNKNOWN
                        : dataset.getDetectedPattern();

        Long recordCount =
                dataset.getRecordCount() == null
                        ? 0L
                        : dataset.getRecordCount();

        Double duplicatePercentage =
                safeDouble(
                        dataset.getDuplicatePercentage()
                );

        Double skewnessValue =
                Math.abs(
                        safeDouble(
                                dataset.getSkewnessValue()
                        )
                );

        Double sortednessScore =
                normalizeSortednessScore(
                        dataset.getSortednessScore()
                );

        if (pattern == DatasetPattern.SKEWED
                || pattern == DatasetPattern.REPEATED_VALUES
                || pattern == DatasetPattern.ADVERSARIAL
                || pattern == DatasetPattern.ZIPF_DISTRIBUTION) {

            return new RecommendationDecision(
                    SortingAlgorithmType
                            .ADAPTIVE_AMPLITUDE_QUICKSORT
                            .name(),
                    92.0,
                    "AAQ is recommended because the dataset pattern is "
                            + pattern.name()
                            + ". Adaptive pivot selection is useful for difficult or imbalanced distributions."
            );
        }

        if (duplicatePercentage >= 20.0) {

            return new RecommendationDecision(
                    SortingAlgorithmType
                            .ADAPTIVE_AMPLITUDE_QUICKSORT
                            .name(),
                    88.0,
                    "AAQ is recommended because duplicate percentage is high: "
                            + duplicatePercentage
                            + "%. Adaptive partition handling is useful for repeated values."
            );
        }

        if (skewnessValue >= 1.0) {

            return new RecommendationDecision(
                    SortingAlgorithmType
                            .ADAPTIVE_AMPLITUDE_QUICKSORT
                            .name(),
                    86.0,
                    "AAQ is recommended because skewness is high: "
                            + skewnessValue
                            + ". Adaptive pivot sampling can reduce bad partitions."
            );
        }

        if (pattern == DatasetPattern.NEARLY_SORTED
                || sortednessScore >= 80.0) {

            return new RecommendationDecision(
                    SortingAlgorithmType
                            .JAVA_BUILT_IN_SORT
                            .name(),
                    84.0,
                    "Java built-in sort is recommended because the dataset appears nearly sorted. Java TimSort performs well on partially sorted data."
            );
        }

        if (recordCount >= 100000) {

            return new RecommendationDecision(
                    SortingAlgorithmType
                            .PARALLEL_SORT
                            .name(),
                    78.0,
                    "Parallel sort is recommended because the dataset is large and suitable for baseline parallel execution."
            );
        }

        if (recordCount <= 10000) {

            return new RecommendationDecision(
                    SortingAlgorithmType
                            .JAVA_BUILT_IN_SORT
                            .name(),
                    80.0,
                    "Java built-in sort is recommended because the dataset is small. AAQ overhead may not provide benefit for small inputs."
            );
        }

        return new RecommendationDecision(
                SortingAlgorithmType
                        .ADAPTIVE_AMPLITUDE_QUICKSORT
                        .name(),
                72.0,
                "AAQ is recommended as the proposed algorithm for general validation and benchmarking."
        );
    }

    private Double safeDouble(
            Double value
    ) {

        if (value == null) {
            return 0.0;
        }

        if (value.isNaN()
                || value.isInfinite()) {
            return 0.0;
        }

        return value;
    }

    private Double normalizeSortednessScore(
            Double sortednessScore
    ) {

        if (sortednessScore == null) {
            return 0.0;
        }

        if (sortednessScore.isNaN()
                || sortednessScore.isInfinite()) {
            return 0.0;
        }

        if (sortednessScore < 0.0) {
            return 0.0;
        }

        /*
         * sortednessScore must be percentage-like: 0 to 100.
         * If old dataset analyzer saved a huge invalid value,
         * ignore it instead of wrongly recommending JAVA_BUILT_IN_SORT.
         */
        if (sortednessScore > 100.0) {
            return 0.0;
        }

        return sortednessScore;
    }

    private AlgorithmRecommendationDto mapToDto(
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

    private record RecommendationDecision(
            String recommendedAlgorithm,
            Double confidenceScore,
            String reason
    ) {
    }
}