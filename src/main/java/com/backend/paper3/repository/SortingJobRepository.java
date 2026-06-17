package com.backend.paper3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.backend.paper3.entity.SortingJobEntity;

@Repository
public interface SortingJobRepository
        extends JpaRepository<SortingJobEntity, Long> {

    Optional<SortingJobEntity> findByJobUniqueId(
            String jobUniqueId
    );

    boolean existsByJobUniqueId(
            String jobUniqueId
    );

    List<SortingJobEntity> findByDatasetIdOrderByCreatedAtDesc(
            Long datasetId
    );

    @Modifying
    @Transactional
    void deleteByDatasetId(Long datasetId);
}