package com.backend.paper3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
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

    // ====================================================
    // CREATE DATASET
    // ====================================================
    @PostMapping(
            value = "/create",
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public DatasetDto createDataset(

            @RequestPart(value = "data", required = false)
            DatasetDto dto,

            @RequestPart(value = "file", required = false)
            MultipartFile file
    ) {

        try {

            // =========================================
            // FILE UPLOAD
            // =========================================
            if (file != null && !file.isEmpty()) {

                String fileName = file.getOriginalFilename();

                if (fileName == null || fileName.isBlank()) {
                    throw new ApiException("Invalid file name");
                }

                fileName = fileName.toLowerCase();

                // CSV
                if (fileName.endsWith(".csv")) {
                    return (DatasetDto) datasetService.createFromCsv(file);
                }

                // XLSX
                if (fileName.endsWith(".xlsx")) {
                    return (DatasetDto) datasetService.createFromXlsx(file);
                }

                throw new ApiException(
                        "Only CSV and XLSX files are allowed"
                );
            }

            // =========================================
            // NORMAL JSON REQUEST
            // =========================================
            if (dto != null) {
                return datasetService.createDataset(dto);
            }

            throw new ApiException(
                    "Please provide dataset JSON or upload a file"
            );

        } catch (Exception e) {

            throw new ApiException(
                    "Dataset creation failed : " + e.getMessage()
            );
        }
    }

    // ====================================================
    // GET ALL DATASETS
    // ====================================================
    @GetMapping("/all")
    public List<DatasetDto> getAllDatasets() {

        return datasetService.getAllDatasets();
    }

    // ====================================================
    // GET DATASET BY ID
    // ====================================================
    @GetMapping("/{id}")
    public DatasetDto getDatasetById(
            @PathVariable Long id
    ) {

        return datasetService.getDatasetById(id);
    }

    // ====================================================
    // DELETE DATASET
    // ====================================================
    @DeleteMapping("/{id}")
    public String deleteDataset(
            @PathVariable Long id
    ) {

        return datasetService.deleteDataset(id);
    }
}