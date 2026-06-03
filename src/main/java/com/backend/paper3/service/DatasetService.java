package com.backend.paper3.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.backend.paper3.dto.DatasetDto;

public interface DatasetService {

	DatasetDto createDataset(DatasetDto dto);

	List<DatasetDto> getAllDatasets();

	DatasetDto getDatasetById(Long id);

	DatasetDto getDatasetByUniqueId(String datasetUniqueId);

	DatasetDto updateDataset(Long id, DatasetDto dto);

	String deleteDataset(Long id);

	DatasetDto createFromCsv(MultipartFile file);

	DatasetDto createFromXlsx(MultipartFile file);
}