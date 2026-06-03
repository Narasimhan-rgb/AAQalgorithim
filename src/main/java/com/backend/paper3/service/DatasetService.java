package com.backend.paper3.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.backend.paper3.dto.DatasetDto;
import com.backend.paper3.dto.DatasetPreviewDto;

public interface DatasetService {

    DatasetDto createDataset(DatasetDto dto);

    List<DatasetDto> getAllDatasets();

    DatasetDto getDatasetById(Long id);

    DatasetDto getDatasetByUniqueId(String datasetUniqueId);

    DatasetPreviewDto previewDataset(Long id);

    DatasetDto updateDataset(Long id, DatasetDto dto);

    String deleteDataset(Long id);

    DatasetDto createFromCsv(MultipartFile file);

    DatasetDto createFromXlsx(MultipartFile file);
}	