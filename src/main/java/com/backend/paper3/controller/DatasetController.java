package com.backend.paper3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import com.backend.paper3.dto.DatasetDto;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.service.DatasetService;

@RestController
@RequestMapping("/dataset")
public class DatasetController {

	@Autowired
	private DatasetService datasetService;

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DatasetDto createDataset(@RequestBody DatasetDto dto) {
		return datasetService.createDataset(dto);
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public DatasetDto uploadDataset(@RequestParam("file") MultipartFile file) {

		if (file == null || file.isEmpty()) {
			throw new ApiException("Please upload a file");
		}

		String fileName = file.getOriginalFilename();

		if (fileName == null || fileName.isBlank()) {
			throw new ApiException("Invalid file name");
		}

		String lowerFileName = fileName.toLowerCase();

		if (lowerFileName.endsWith(".csv")) {
			return datasetService.createFromCsv(file);
		}

		if (lowerFileName.endsWith(".xlsx")) {
			return datasetService.createFromXlsx(file);
		}

		throw new ApiException("Only CSV and XLSX files are allowed");
	}

	@GetMapping("/all")
	public List<DatasetDto> getAllDatasets() {
		return datasetService.getAllDatasets();
	}

	@GetMapping("/{id}")
	public DatasetDto getDatasetById(@PathVariable Long id) {
		return datasetService.getDatasetById(id);
	}

	@GetMapping("/unique/{datasetUniqueId}")
	public DatasetDto getDatasetByUniqueId(@PathVariable String datasetUniqueId) {
		return datasetService.getDatasetByUniqueId(datasetUniqueId);
	}

	@PutMapping("/{id}")
	public DatasetDto updateDataset(@PathVariable Long id, @RequestBody DatasetDto dto) {
		return datasetService.updateDataset(id, dto);
	}

	@DeleteMapping("/{id}")
	public String deleteDataset(@PathVariable Long id) {
		return datasetService.deleteDataset(id);
	}
}