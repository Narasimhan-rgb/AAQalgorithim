package com.backend.paper3.serviceimpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.SortingJobDto;
import com.backend.paper3.entity.DatasetEntity;
import com.backend.paper3.entity.SortingJobEntity;
import com.backend.paper3.enums.SortingAlgorithmType;
import com.backend.paper3.enums.SortingJobStatus;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.repository.DatasetRepository;
import com.backend.paper3.repository.SortingJobRepository;
import com.backend.paper3.service.SortingJobService;

@Service
public class SortingJobServiceImpl
        implements SortingJobService {

    @Autowired
    private SortingJobRepository sortingJobRepository;

    @Autowired
    private DatasetRepository datasetRepository;

    @Override
    public SortingJobDto startJob(
            SortingJobDto dto
    ) {

        validateStartJobRequest(dto);

        DatasetEntity dataset =
                datasetRepository
                        .findById(dto.getDatasetId())
                        .orElseThrow(
                                () -> new ApiException(
                                        "Dataset not found with id : "
                                                + dto.getDatasetId()
                                )
                        );

        SortingJobEntity entity =
                new SortingJobEntity();

        entity.setJobUniqueId(
                generateUniqueJobId(
                        dataset.getDatasetName()
                )
        );

        entity.setDatasetId(
                dataset.getId()
        );

        entity.setRequestedAlgorithm(
                normalizeAlgorithm(dto)
        );

        entity.setRecommendedAlgorithm(
                null
        );

        entity.setSelectedColumn(
                resolveSelectedColumn(
                        dto.getSelectedColumn(),
                        dataset.getSelectedSortColumn()
                )
        );

        entity.setStatus(
                SortingJobStatus.PENDING
        );

        entity.setProgressPercentage(
                0
        );

        entity.setCreatedBy(
                dto.getCreatedBy()
        );

        entity.setCreatedAt(
                LocalDateTime.now()
        );

        SortingJobEntity savedEntity =
                sortingJobRepository.save(entity);

        return mapToDto(
                savedEntity,
                dataset
        );
    }

    @Override
    public List<SortingJobDto> getAllJobs() {

        return sortingJobRepository
                .findAll()
                .stream()
                .map(this::mapToDtoWithDataset)
                .collect(Collectors.toList());
    }

    @Override
    public SortingJobDto getJobById(
            Long id
    ) {

        SortingJobEntity entity =
                findJobOrThrow(id);

        return mapToDtoWithDataset(entity);
    }

    @Override
    public SortingJobDto getJobStatus(
            Long id
    ) {

        SortingJobEntity entity =
                findJobOrThrow(id);

        return mapToDtoWithDataset(entity);
    }

    @Override
    public SortingJobDto cancelJob(
            Long id
    ) {

        SortingJobEntity entity =
                findJobOrThrow(id);

        if (entity.getStatus() == SortingJobStatus.COMPLETED) {
            throw new ApiException(
                    "Completed job cannot be cancelled"
            );
        }

        if (entity.getStatus() == SortingJobStatus.FAILED) {
            throw new ApiException(
                    "Failed job cannot be cancelled"
            );
        }

        if (entity.getStatus() == SortingJobStatus.CANCELLED) {
            throw new ApiException(
                    "Job is already cancelled"
            );
        }

        entity.setStatus(
                SortingJobStatus.CANCELLED
        );

        entity.setProgressPercentage(
                0
        );

        entity.setCompletedAt(
                LocalDateTime.now()
        );

        SortingJobEntity savedEntity =
                sortingJobRepository.save(entity);

        return mapToDtoWithDataset(savedEntity);
    }

    private void validateStartJobRequest(
            SortingJobDto dto
    ) {

        if (dto == null) {
            throw new ApiException(
                    "Sorting job request body is required"
            );
        }

        if (dto.getDatasetId() == null) {
            throw new ApiException(
                    "Dataset id is required"
            );
        }
    }

    private SortingJobEntity findJobOrThrow(
            Long id
    ) {

        if (id == null) {
            throw new ApiException(
                    "Job id is required"
            );
        }

        return sortingJobRepository
                .findById(id)
                .orElseThrow(
                        () -> new ApiException(
                                "Sorting job not found with id : " + id
                        )
                );
    }

    private String normalizeAlgorithm(
            SortingJobDto dto
    ) {

        String requestedAlgorithm =
                dto.getRecommendedAlgorithm();

        if (requestedAlgorithm == null
                || requestedAlgorithm.trim().isEmpty()) {

            requestedAlgorithm =
                    dto.getRequestedAlgorithm();
        }

        if (requestedAlgorithm == null
                || requestedAlgorithm.trim().isEmpty()) {

            return SortingAlgorithmType
                    .ADAPTIVE_AMPLITUDE_QUICKSORT
                    .name();
        }

        String algorithm =
                requestedAlgorithm
                        .trim()
                        .toUpperCase();

        try {

            SortingAlgorithmType.valueOf(algorithm);

            return algorithm;

        } catch (Exception e) {

            throw new ApiException(
                    "Invalid sorting algorithm : " + requestedAlgorithm
            );
        }
    }

    private String resolveSelectedColumn(
            String requestColumn,
            String datasetColumn
    ) {

        if (requestColumn != null
                && !requestColumn.trim().isEmpty()) {

            return requestColumn.trim();
        }

        if (datasetColumn != null
                && !datasetColumn.trim().isEmpty()) {

            return datasetColumn.trim();
        }

        return "AUTO_DETECTED_FIRST_COLUMN";
    }

    private SortingJobDto mapToDtoWithDataset(
            SortingJobEntity entity
    ) {

        DatasetEntity dataset =
                datasetRepository
                        .findById(entity.getDatasetId())
                        .orElse(null);

        return mapToDto(
                entity,
                dataset
        );
    }

    private SortingJobDto mapToDto(
            SortingJobEntity entity,
            DatasetEntity dataset
    ) {

        SortingJobDto dto =
                new SortingJobDto();

        dto.setId(entity.getId());

        dto.setJobUniqueId(
                entity.getJobUniqueId()
        );

        dto.setDatasetId(
                entity.getDatasetId()
        );

        dto.setAlgorithm(
                entity.getRequestedAlgorithm()
        );

        dto.setRequestedAlgorithm(
                entity.getRequestedAlgorithm()
        );

        dto.setRecommendedAlgorithm(
                entity.getRecommendedAlgorithm()
        );

        dto.setStatus(
                entity.getStatus()
        );

        dto.setProgressPercentage(
                entity.getProgressPercentage()
        );

        dto.setSelectedColumn(
                entity.getSelectedColumn()
        );

        dto.setStartedAt(
                entity.getStartedAt()
        );

        dto.setCompletedAt(
                entity.getCompletedAt()
        );

        dto.setErrorMessage(
                entity.getErrorMessage()
        );

        dto.setCreatedBy(
                entity.getCreatedBy()
        );

        dto.setCreatedAt(
                entity.getCreatedAt()
        );

        if (dataset != null) {

            dto.setDatasetName(
                    dataset.getDatasetName()
            );

            dto.setDatasetUniqueId(
                    dataset.getDatasetUniqueId()
            );
        }

        return dto;
    }

    private String generateUniqueJobId(
            String datasetName
    ) {

        String safeName =
                datasetName == null
                        ? "DATASET"
                        : datasetName
                                .trim()
                                .toUpperCase()
                                .replaceAll("[^A-Z0-9]", "_")
                                .replaceAll("_+", "_");

        if (safeName.isBlank()) {
            safeName = "DATASET";
        }

        if (safeName.length() > 18) {
            safeName = safeName.substring(0, 18);
        }

        String dateTime =
                LocalDateTime
                        .now()
                        .format(
                                DateTimeFormatter
                                        .ofPattern("yyyyMMddHHmmss")
                        );

        String jobUniqueId;

        do {

            String random =
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0, 8)
                            .toUpperCase();

            jobUniqueId =
                    "JOB-" + safeName + "-" + dateTime + "-" + random;

        } while (
                sortingJobRepository.existsByJobUniqueId(
                        jobUniqueId
                )
        );

        return jobUniqueId;
    }
}