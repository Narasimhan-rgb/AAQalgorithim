package com.backend.paper3.mapper;

import java.time.LocalDateTime;

import com.backend.paper3.dto.AuthDto;
import com.backend.paper3.dto.DatasetDto;
import com.backend.paper3.entity.DatasetEntity;
import com.backend.paper3.entity.UserEntity;

public class AppMapper {

    public static UserEntity
    mapToUserEntity(AuthDto dto) {

        return UserEntity.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .passwordHash(dto.getPassword())
                .role(dto.getRole())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public DatasetDto toDto(DatasetEntity savedEntity) {

        if (savedEntity == null) {
            return null;
        }

        DatasetDto dto = new DatasetDto();

        dto.setId(savedEntity.getId());
        dto.setDatasetName(savedEntity.getDatasetName());

        dto.setValue(savedEntity.getValue());

        dto.setDuplicatePercentage(savedEntity.getDuplicatePercentage());
        dto.setNullPercentage(savedEntity.getNullPercentage());

        dto.setSortednessScore(savedEntity.getSortednessScore());

        dto.setQuantumScore(savedEntity.getQuantumScore());
        dto.setFinalScore(savedEntity.getFinalScore());

        dto.setCreatedAt(savedEntity.getCreatedAt());

        return dto;
    }
}