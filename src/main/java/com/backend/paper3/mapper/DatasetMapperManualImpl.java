package com.backend.paper3.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.backend.paper3.dto.DatasetDto;
import com.backend.paper3.entity.DatasetEntity;

@Component
@Primary
public class DatasetMapperManualImpl implements DatasetMapper {

    @Override
    public DatasetDto toDto(DatasetEntity entity) {

        if (entity == null) {
            return null;
        }

        DatasetDto dto = new DatasetDto();

        BeanUtils.copyProperties(
                entity,
                dto
        );

        return dto;
    }

    @Override
    public DatasetEntity toEntity(DatasetDto dto) {

        if (dto == null) {
            return null;
        }

        DatasetEntity entity = new DatasetEntity();

        BeanUtils.copyProperties(
                dto,
                entity
        );

        return entity;
    }
}