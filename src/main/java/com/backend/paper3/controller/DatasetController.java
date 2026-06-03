package com.backend.paper3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.backend.paper3.dto.DatasetDto;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.service.DatasetService;

@RestController
@RequestMapping("/dataset")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;

    @PostMapping(
            value = "/create",
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public DatasetDto createDataset(
            @RequestPart(value = "data", required = false) DatasetDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {

        if (file != null && !file.isEmpty()) {

            String fileName = file.getOriginalFilename();

            if (fileName == null || fileName.isBlank()) {
                throw new ApiException("Invalid file name");
            }

            fileName = fileName.toLowerCase();

            if (fileName.endsWith(".csv")) {
                return datasetService.createFromCsv(file);
            }

            if (fileName.endsWith(".xlsx")) {
                return datasetService.createFromXlsx(file);
            }

            throw new ApiException("Only CSV and XLSX files are allowed");
        }

        if (dto != null) {
            return datasetService.createDataset(dto);
        }

        throw new ApiException("Please provide dataset JSON or upload a file");
    }

    @GetMapping("/all")
    public List<DatasetDto> getAllDatasets() {
        return datasetService.getAllDatasets();
    }

    @GetMapping("/{id}")
    public DatasetDto getDatasetById(@PathVariable Long id) {
        return datasetService.getDatasetById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteDataset(@PathVariable Long id) {
        return datasetService.deleteDataset(id);
    }
}