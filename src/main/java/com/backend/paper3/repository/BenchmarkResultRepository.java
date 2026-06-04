package com.backend.paper3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.paper3.entity.BenchmarkResultEntity;

@Repository
public interface BenchmarkResultRepository
        extends JpaRepository<BenchmarkResultEntity, Long> {

    List<BenchmarkResultEntity> findByJobIdOrderByCreatedAtDesc(
            Long jobId
    );

    List<BenchmarkResultEntity> findByDatasetIdOrderByCreatedAtDesc(
            Long datasetId
    );

    List<BenchmarkResultEntity> findByAlgorithmOrderByCreatedAtDesc(
            String algorithm
    );
}