package com.backend.paper3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.paper3.entity.ReportEntity;

@Repository
public interface ReportRepository
        extends JpaRepository<ReportEntity, Long> {

    List<ReportEntity> findByJobIdOrderByCreatedAtDesc(
            Long jobId
    );

    List<ReportEntity> findByDatasetIdOrderByCreatedAtDesc(
            Long datasetId
    );
}