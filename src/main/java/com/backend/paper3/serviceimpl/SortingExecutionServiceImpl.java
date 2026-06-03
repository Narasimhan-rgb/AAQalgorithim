package com.backend.paper3.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.dto.SortingRunRequestDto;
import com.backend.paper3.dto.SortingRunResultDto;
import com.backend.paper3.entity.DatasetEntity;
import com.backend.paper3.entity.SortingJobEntity;
import com.backend.paper3.enums.SortingAlgorithmType;
import com.backend.paper3.enums.SortingJobStatus;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.repository.DatasetRepository;
import com.backend.paper3.repository.SortingJobRepository;
import com.backend.paper3.service.SortingExecutionService;
import com.backend.paper3.util.DatasetColumnReaderUtil;

@Service
public class SortingExecutionServiceImpl
        implements SortingExecutionService {

    @Autowired
    private SortingJobRepository sortingJobRepository;

    @Autowired
    private DatasetRepository datasetRepository;

    @Override
    public SortingRunResultDto runSorting(
            SortingRunRequestDto request
    ) {

        if (request == null || request.getJobId() == null) {
            throw new ApiException("Job id is required");
        }

        SortingJobEntity job =
                sortingJobRepository
                        .findById(request.getJobId())
                        .orElseThrow(
                                () -> new ApiException(
                                        "Sorting job not found with id : "
                                                + request.getJobId()
                                )
                        );

        DatasetEntity dataset =
                datasetRepository
                        .findById(job.getDatasetId())
                        .orElseThrow(
                                () -> new ApiException(
                                        "Dataset not found with id : "
                                                + job.getDatasetId()
                                )
                        );

        if (job.getStatus() == SortingJobStatus.CANCELLED) {
            throw new ApiException(
                    "Cancelled job cannot be executed"
            );
        }

        LocalDateTime startedAt =
                LocalDateTime.now();

        job.setStatus(
                SortingJobStatus.RUNNING
        );

        job.setStartedAt(
                startedAt
        );

        job.setProgressPercentage(
                10
        );

        sortingJobRepository.save(job);

        try {

            String algorithm =
                    resolveAlgorithm(
                            job.getRequestedAlgorithm()
                    );

            List<String> values =
                    DatasetColumnReaderUtil.readColumnValues(
                            dataset.getFilePath(),
                            dataset.getFileType(),
                            job.getSelectedColumn()
                    );

            long startTime =
                    System.nanoTime();

            List<String> sortedValues =
                    new ArrayList<>(values);

            Collections.sort(sortedValues);

            long endTime =
                    System.nanoTime();

            long executionTimeMs =
                    (endTime - startTime) / 1_000_000;

            LocalDateTime completedAt =
                    LocalDateTime.now();

            job.setStatus(
                    SortingJobStatus.COMPLETED
            );

            job.setProgressPercentage(
                    100
            );

            job.setCompletedAt(
                    completedAt
            );

            job.setErrorMessage(
                    null
            );

            sortingJobRepository.save(job);

            return buildResult(
                    job,
                    dataset,
                    algorithm,
                    sortedValues,
                    executionTimeMs,
                    startedAt,
                    completedAt
            );

        } catch (ApiException e) {

            job.setStatus(
                    SortingJobStatus.FAILED
            );

            job.setProgressPercentage(
                    0
            );

            job.setErrorMessage(
                    e.getMessage()
            );

            job.setCompletedAt(
                    LocalDateTime.now()
            );

            sortingJobRepository.save(job);

            throw e;

        } catch (Exception e) {

            job.setStatus(
                    SortingJobStatus.FAILED
            );

            job.setProgressPercentage(
                    0
            );

            job.setErrorMessage(
                    e.getMessage()
            );

            job.setCompletedAt(
                    LocalDateTime.now()
            );

            sortingJobRepository.save(job);

            throw new ApiException(
                    "Sorting execution failed : " + e.getMessage()
            );
        }
    }

    private String resolveAlgorithm(
            String requestedAlgorithm
    ) {

        if (requestedAlgorithm == null
                || requestedAlgorithm.trim().isEmpty()) {

            return SortingAlgorithmType
                    .JAVA_BUILT_IN_SORT
                    .name();
        }

        String algorithm =
                requestedAlgorithm
                        .trim()
                        .toUpperCase();

        if (algorithm.equals(
                SortingAlgorithmType.ADAPTIVE_AMPLITUDE_QUICKSORT.name()
        )) {

            return SortingAlgorithmType
                    .JAVA_BUILT_IN_SORT
                    .name();
        }

        if (algorithm.equals(
                SortingAlgorithmType.JAVA_BUILT_IN_SORT.name()
        )) {

            return SortingAlgorithmType
                    .JAVA_BUILT_IN_SORT
                    .name();
        }

        throw new ApiException(
                "Only JAVA_BUILT_IN_SORT is executable in this module. AAQ comes in next module."
        );
    }

    private SortingRunResultDto buildResult(
            SortingJobEntity job,
            DatasetEntity dataset,
            String algorithm,
            List<String> sortedValues,
            long executionTimeMs,
            LocalDateTime startedAt,
            LocalDateTime completedAt
    ) {

        SortingRunResultDto result =
                new SortingRunResultDto();

        result.setJobId(
                job.getId()
        );

        result.setJobUniqueId(
                job.getJobUniqueId()
        );

        result.setDatasetId(
                dataset.getId()
        );

        result.setDatasetName(
                dataset.getDatasetName()
        );

        result.setDatasetUniqueId(
                dataset.getDatasetUniqueId()
        );

        result.setAlgorithm(
                algorithm
        );

        result.setSelectedColumn(
                job.getSelectedColumn()
        );

        result.setTotalValuesSorted(
                (long) sortedValues.size()
        );

        result.setExecutionTimeMs(
                executionTimeMs
        );

        result.setStatus(
                SortingJobStatus.COMPLETED
        );

        result.setStartedAt(
                startedAt
        );

        result.setCompletedAt(
                completedAt
        );

        int previewLimit =
                Math.min(
                        sortedValues.size(),
                        10
                );

        result.setSortedPreview(
                sortedValues.subList(
                        0,
                        previewLimit
                )
        );

        return result;
    }
}