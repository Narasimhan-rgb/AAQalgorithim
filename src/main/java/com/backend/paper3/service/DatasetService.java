package com.backend.paper3.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.backend.paper3.dto.DatasetDto;

public interface DatasetService {

    DatasetDto createDataset(DatasetDto dto);

    List<DatasetDto> getAllDatasets();

    DatasetDto getDatasetById(Long id);

    DatasetDto updateDataset(Long id, DatasetDto dto);

    String deleteDataset(Long id);

    // FILE METHODS

    List<DatasetDto> createFromCsv(MultipartFile file);

    List<DatasetDto> createFromXlsx(MultipartFile file);
}