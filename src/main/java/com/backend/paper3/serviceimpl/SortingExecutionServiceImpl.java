package com.backend.paper3.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.paper3.algorithm.SortingAlgorithmResult;
import com.backend.paper3.algorithm.SortingEngine;
import com.backend.paper3.dto.SortingRunRequestDto;
import com.backend.paper3.dto.SortingRunResultDto;
import com.backend.paper3.entity.DatasetEntity;
import com.backend.paper3.entity.SortingJobEntity;
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

            List<String> values =
                    DatasetColumnReaderUtil.readColumnValues(
                            dataset.getFilePath(),
                            dataset.getFileType(),
                            job.getSelectedColumn()
                    );

            SortingEngine sortingEngine =
                    new SortingEngine();

            SortingAlgorithmResult algorithmResult =
                    sortingEngine.execute(
                            values,
                            job.getRequestedAlgorithm()
                    );

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
                    algorithmResult,
                    startedAt,
                    completedAt
            );

        } catch (ApiException e) {

            markJobFailed(
                    job,
                    e.getMessage()
            );

            throw e;

        } catch (Exception e) {

            markJobFailed(
                    job,
                    e.getMessage()
            );

            throw new ApiException(
                    "Sorting execution failed : " + e.getMessage()
            );
        }
    }

    private void markJobFailed(
            SortingJobEntity job,
            String errorMessage
    ) {

        job.setStatus(
                SortingJobStatus.FAILED
        );

        job.setProgressPercentage(
                0
        );

        job.setErrorMessage(
                errorMessage
        );

        job.setCompletedAt(
                LocalDateTime.now()
        );

        sortingJobRepository.save(job);
    }

    private SortingRunResultDto buildResult(
            SortingJobEntity job,
            DatasetEntity dataset,
            SortingAlgorithmResult algorithmResult,
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
                algorithmResult.getAlgorithmName()
        );

        result.setSelectedColumn(
                job.getSelectedColumn()
        );

        result.setTotalValuesSorted(
                algorithmResult.getInputSize()
        );

        result.setExecutionTimeMs(
                algorithmResult.getExecutionTimeMs()
        );

        result.setComparisonCount(
                algorithmResult.getComparisonCount()
        );

        result.setSwapCount(
                algorithmResult.getSwapCount()
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

        List<String> sortedValues =
                algorithmResult.getSortedValues();

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