package com.backend.paper3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.paper3.entity.DatasetEntity;

@Repository
public interface DatasetRepository extends JpaRepository<DatasetEntity, Long> {

	boolean existsByOriginalFileNameAndFileSizeBytes(String originalFileName, Long fileSizeBytes);

	Optional<DatasetEntity> findByOriginalFileNameAndFileSizeBytes(String originalFileName, Long fileSizeBytes);

	boolean existsByDatasetUniqueId(String datasetUniqueId);

	Optional<DatasetEntity> findByDatasetUniqueId(String datasetUniqueId);
}