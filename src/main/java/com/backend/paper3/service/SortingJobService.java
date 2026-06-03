package com.backend.paper3.service;

import java.util.List;

import com.backend.paper3.dto.SortingJobDto;

public interface SortingJobService {

    SortingJobDto startJob(
            SortingJobDto dto
    );

    List<SortingJobDto> getAllJobs();

    SortingJobDto getJobById(
            Long id
    );

    SortingJobDto getJobStatus(
            Long id
    );

    SortingJobDto cancelJob(
            Long id
    );
}