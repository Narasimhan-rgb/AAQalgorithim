package com.backend.paper3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.backend.paper3.entity.QuantumAaqMetricsEntity;

@Repository
public interface QuantumAaqMetricsRepository
        extends JpaRepository<QuantumAaqMetricsEntity, Long> {

    List<QuantumAaqMetricsEntity> findByJobIdOrderByCreatedAtDesc(
            Long jobId
    );

    List<QuantumAaqMetricsEntity> findByDatasetIdOrderByCreatedAtDesc(
            Long datasetId
    );

    @Modifying
    @Transactional
    void deleteByDatasetId(Long datasetId);
}