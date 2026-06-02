package com.backend.paper3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.paper3.entity.DatasetEntity;

@Repository
public interface DatasetRepository
        extends JpaRepository<DatasetEntity, Long> {

}