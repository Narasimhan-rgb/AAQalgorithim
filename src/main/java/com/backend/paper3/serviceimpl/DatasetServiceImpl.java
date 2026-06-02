
package com.backend.paper3.serviceimpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.paper3.algorithm.DatasetScoreAlgorithm;
import com.backend.paper3.dto.DatasetDto;
import com.backend.paper3.entity.DatasetEntity;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.mapper.DatasetMapper;
import com.backend.paper3.quantum.QuantumDatasetAnalyzer;
import com.backend.paper3.repository.DatasetRepository;
import com.backend.paper3.service.DatasetService;

@Service
public class DatasetServiceImpl implements DatasetService {

    @Autowired
    private DatasetRepository datasetRepository;

    
    private final DatasetMapper appMapper;

    @Autowired
    private QuantumDatasetAnalyzer quantumAnalyzer;

    public DatasetServiceImpl(
            DatasetRepository datasetRepository,
            DatasetMapper datasetMapper
    ) {
        this.datasetRepository = datasetRepository;
        this.datasetMapper = datasetMapper;
    }
    // =========================================
    // CREATE DATASET
    // =========================================

    @Override
    public DatasetDto createDataset(DatasetDto dto) {

        try {

            if (dto == null) {
                throw new ApiException("Dataset payload is null");
            }

            if (dto.getDatasetName() == null
                    || dto.getDatasetName().trim().isEmpty()) {

                throw new ApiException("Dataset name is required");
            }

            double quantumScore =
                    quantumAnalyzer.calculateQuantumScore(
                            dto.getValue()
                    );

            double sortednessScore =
                    DatasetScoreAlgorithm.calculateSortedness(dto);

            double finalScore =
                    (quantumScore + sortednessScore) / 2.0;

            DatasetEntity entity = new DatasetEntity();

            entity.setDatasetName(dto.getDatasetName());
            entity.setOriginalFileName(dto.getOriginalFileName());
            entity.setFilePath(dto.getFilePath());
            entity.setFileType(dto.getFileType());
            entity.setFileSizeBytes(dto.getFileSizeBytes());

            entity.setRecordCount(dto.getRecordCount());
            entity.setColumnCount(dto.getColumnCount());

            entity.setNullPercentage(dto.getNullPercentage());
            entity.setDuplicatePercentage(dto.getDuplicatePercentage());

            entity.setQuantumScore(quantumScore);
            entity.setSortednessScore(sortednessScore);
            entity.setFinalScore(finalScore);

            entity.setValue(dto.getValue());

            entity.setCreatedAt(LocalDateTime.now());

            DatasetEntity savedEntity =
                    datasetRepository.save(entity);

            return appMapper.toDto(savedEntity);

        } catch (Exception e) {

            throw new ApiException(
                    "Failed to create dataset : "
                            + e.getMessage()
            );
        }
    }

    // =========================================
    // GET ALL DATASETS
    // =========================================

    @Override
    public List<DatasetDto> getAllDatasets() {

        List<DatasetEntity> entityList =
                datasetRepository.findAll();

        return entityList.stream()
                .map(appMapper::toDto)
                .collect(Collectors.toList());
    }

    // =========================================
    // GET DATASET BY ID
    // =========================================

    @Override
    public DatasetDto getDatasetById(Long id) {

        DatasetEntity entity = datasetRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                "Dataset not found with id : " + id
                        )
                );

        return appMapper.toDto(entity);
    }

    // =========================================
    // UPDATE DATASET
    // =========================================

    @Override
    public DatasetDto updateDataset(Long id, DatasetDto dto) {

        DatasetEntity entity = datasetRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                "Dataset not found with id : " + id
                        )
                );

        entity.setDatasetName(dto.getDatasetName());
        entity.setOriginalFileName(dto.getOriginalFileName());
        entity.setFilePath(dto.getFilePath());
        entity.setFileType(dto.getFileType());
        entity.setFileSizeBytes(dto.getFileSizeBytes());

        entity.setRecordCount(dto.getRecordCount());
        entity.setColumnCount(dto.getColumnCount());

        entity.setNullPercentage(dto.getNullPercentage());
        entity.setDuplicatePercentage(dto.getDuplicatePercentage());

        double quantumScore =
                quantumAnalyzer.calculateQuantumScore(
                        dto.getValue()
                );

        double sortednessScore =
                DatasetScoreAlgorithm.calculateSortedness(dto);

        double finalScore =
                (quantumScore + sortednessScore) / 2.0;

        entity.setQuantumScore(quantumScore);
        entity.setSortednessScore(sortednessScore);
        entity.setFinalScore(finalScore);

        entity.setValue(dto.getValue());

        DatasetEntity updatedEntity =
                datasetRepository.save(entity);

        return appMapper.toDto(updatedEntity);
    }

    // =========================================
    // DELETE DATASET
    // =========================================

    @Override
    public String deleteDataset(Long id) {

        DatasetEntity entity = datasetRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                "Dataset not found with id : " + id
                        )
                );

        datasetRepository.delete(entity);

        return "Dataset deleted successfully";
    }

    // =========================================
    // CSV FILE UPLOAD
    // =========================================

    @Override
    public List<DatasetDto> createFromCsv(
            MultipartFile file
    ) {

        try {

            List<DatasetDto> savedList =
                    new ArrayList<>();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            file.getInputStream()
                    )
            );

            String line;

            int rowCount = 0;

            while ((line = br.readLine()) != null) {

                rowCount++;

                // Skip header
                if (rowCount == 1) {
                    continue;
                }

                String[] data = line.split(",");

                DatasetDto dto = new DatasetDto();

                dto.setDatasetName(data[0]);

                dto.setOriginalFileName(
                        file.getOriginalFilename()
                );

                dto.setFileType("CSV");

                dto.setFileSizeBytes(file.getSize());

                dto.setRecordCount(
                        Integer.parseInt(data[1])
                );

                dto.setColumnCount(
                        Integer.parseInt(data[2])
                );

                dto.setNullPercentage(
                        Double.parseDouble(data[3])
                );

                dto.setDuplicatePercentage(
                        Double.parseDouble(data[4])
                );

                dto.setValue(
                        Double.parseDouble(data[5])
                );

                savedList.add(
                        createDataset(dto)
                );
            }

            br.close();

            return savedList;

        } catch (Exception e) {

            throw new ApiException(
                    "CSV upload failed : "
                            + e.getMessage()
            );
        }
    }

    // =========================================
    // XLSX FILE UPLOAD
    // =========================================

    @Override
    public List<DatasetDto> createFromXlsx(
            MultipartFile file
    ) {

        try {

            List<DatasetDto> savedList =
                    new ArrayList<>();

            Workbook workbook =
                    new XSSFWorkbook(
                            file.getInputStream()
                    );

            Sheet sheet =
                    workbook.getSheetAt(0);

            int rowCount = 0;

            for (Row row : sheet) {

                rowCount++;

                // Skip header
                if (rowCount == 1) {
                    continue;
                }

                DatasetDto dto = new DatasetDto();

                dto.setDatasetName(
                        row.getCell(0).getStringCellValue()
                );

                dto.setOriginalFileName(
                        file.getOriginalFilename()
                );

                dto.setFileType("XLSX");

                dto.setFileSizeBytes(file.getSize());

                dto.setRecordCount(
                        (int) row.getCell(1)
                                .getNumericCellValue()
                );

                dto.setColumnCount(
                        (int) row.getCell(2)
                                .getNumericCellValue()
                );

                dto.setNullPercentage(
                        row.getCell(3)
                                .getNumericCellValue()
                );

                dto.setDuplicatePercentage(
                        row.getCell(4)
                                .getNumericCellValue()
                );

                dto.setValue(
                        row.getCell(5)
                                .getNumericCellValue()
                );

                savedList.add(
                        createDataset(dto)
                );
            }

            workbook.close();

            return savedList;

        } catch (Exception e) {

            throw new ApiException(
                    "XLSX upload failed : "
                            + e.getMessage()
            );
        }
    }
}

