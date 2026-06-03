package com.backend.paper3.mapper;

import org.mapstruct.Mapper;

import com.backend.paper3.dto.DatasetDto;
import com.backend.paper3.entity.DatasetEntity;

@Mapper(componentModel = "spring")
public interface DatasetMapper {

    DatasetDto toDto(DatasetEntity entity);

    DatasetEntity toEntity(DatasetDto dto);
}