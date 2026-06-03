package com.backend.paper3.serviceimpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.DataFormatter;
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
import com.backend.paper3.service.LocalDatasetStorageService;

@Service
public class DatasetServiceImpl implements DatasetService {

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private DatasetMapper appMapper;

    @Autowired
    private QuantumDatasetAnalyzer quantumAnalyzer;

    @Autowired
    private LocalDatasetStorageService localDatasetStorageService;

    @Override
    public DatasetDto createDataset(DatasetDto dto) {

        try {

            if (dto == null) {
                throw new ApiException("Dataset payload is null");
            }

            if (dto.getDatasetName() == null || dto.getDatasetName().trim().isEmpty()) {
                throw new ApiException("Dataset name is required");
            }

            double value = dto.getValue() == null ? 0.0 : dto.getValue();

            dto.setValue(value);

            double quantumScore = quantumAnalyzer.calculateQuantumScore(value);

            double sortednessScore = DatasetScoreAlgorithm.calculateSortedness(dto);

            double finalScore = (quantumScore + sortednessScore) / 2.0;

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

            entity.setValue(value);
            entity.setQuantumScore(quantumScore);
            entity.setSortednessScore(sortednessScore);
            entity.setFinalScore(finalScore);

            entity.setCreatedAt(LocalDateTime.now());

            DatasetEntity savedEntity = datasetRepository.save(entity);

            return appMapper.toDto(savedEntity);

        } catch (ApiException e) {

            throw e;

        } catch (Exception e) {

            throw new ApiException("Failed to create dataset : " + e.getMessage());
        }
    }

    @Override
    public DatasetDto createFromCsv(MultipartFile file) {

        try {

            String storedPath = localDatasetStorageService.storeFile(file);

            DatasetDto dto = analyzeCsv(file);

            dto.setDatasetName(removeExtension(file.getOriginalFilename()));
            dto.setOriginalFileName(file.getOriginalFilename());
            dto.setFilePath(storedPath);
            dto.setFileType("CSV");
            dto.setFileSizeBytes(file.getSize());

            return createDataset(dto);

        } catch (Exception e) {

            throw new ApiException("CSV upload failed : " + e.getMessage());
        }
    }

    @Override
   
    public DatasetDto createFromXlsx(MultipartFile file) {

        try {

            String storedPath = localDatasetStorageService.storeFile(file);

            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = 0;
            int columnCount = 0;

            boolean headerSkipped = false;

            for (Row row : sheet) {

                if (row == null) {
                    continue;
                }

                if (!headerSkipped) {
                    columnCount = row.getLastCellNum();
                    headerSkipped = true;
                    continue;
                }

                if (row.getLastCellNum() > 0) {
                    rowCount++;
                    columnCount = Math.max(columnCount, row.getLastCellNum());
                }
            }

            workbook.close();

            DatasetDto dto = new DatasetDto();

            dto.setDatasetName(removeExtension(file.getOriginalFilename()));
            dto.setOriginalFileName(file.getOriginalFilename());
            dto.setFilePath(storedPath);
            dto.setFileType("XLSX");
            dto.setFileSizeBytes(file.getSize());

            dto.setRecordCount((long) rowCount);
            dto.setColumnCount(columnCount);

            dto.setNullPercentage(0.0);
            dto.setDuplicatePercentage(0.0);
            dto.setValue(1.0);

            return createDataset(dto);

        } catch (Exception e) {

            throw new ApiException("XLSX upload failed : " + e.getMessage());
        }
    }

    private DatasetDto analyzeCsv(MultipartFile file) {

        try {

            DatasetDto dto = new DatasetDto();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(file.getInputStream())
            );

            String line;

            int rowCount = 0;
            int columnCount = 0;

            int totalCells = 0;
            int nullCells = 0;

            double numericSum = 0.0;
            int numericCount = 0;

            Set<String> uniqueRows = new HashSet<>();

            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] cells = line.split(",", -1);

                if (!headerSkipped) {
                    columnCount = cells.length;
                    headerSkipped = true;
                    continue;
                }

                rowCount++;

                uniqueRows.add(line.trim());

                columnCount = Math.max(columnCount, cells.length);

                for (String cell : cells) {

                    totalCells++;

                    String value = cell == null ? "" : cell.trim();

                    if (value.isEmpty()) {
                        nullCells++;
                    } else {
                        Double number = tryParseDouble(value);

                        if (number != null) {
                            numericSum += number;
                            numericCount++;
                        }
                    }
                }
            }

            br.close();

            fillMetadata(
                    dto,
                    rowCount,
                    columnCount,
                    totalCells,
                    nullCells,
                    numericSum,
                    numericCount,
                    uniqueRows.size()
            );

            return dto;

        } catch (Exception e) {

            throw new ApiException("CSV analysis failed : " + e.getMessage());
        }
    }

    private DatasetDto analyzeXlsx(MultipartFile file) {

        try {

            DatasetDto dto = new DatasetDto();

            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();

            int rowCount = 0;
            int columnCount = 0;

            int totalCells = 0;
            int nullCells = 0;

            double numericSum = 0.0;
            int numericCount = 0;

            Set<String> uniqueRows = new HashSet<>();

            boolean headerSkipped = false;

            for (Row row : sheet) {

                if (row == null) {
                    continue;
                }

                int lastCellNumber = row.getLastCellNum();

                if (lastCellNumber <= 0) {
                    continue;
                }

                columnCount = Math.max(columnCount, lastCellNumber);

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                StringBuilder rowData = new StringBuilder();

                boolean emptyRow = true;

                for (int i = 0; i < lastCellNumber; i++) {

                    String cellValue = formatter
                            .formatCellValue(row.getCell(i))
                            .trim();

                    rowData.append(cellValue).append("|");

                    if (!cellValue.isEmpty()) {
                        emptyRow = false;
                    }
                }

                if (emptyRow) {
                    continue;
                }

                rowCount++;

                uniqueRows.add(rowData.toString());

                for (int i = 0; i < lastCellNumber; i++) {

                    totalCells++;

                    String cellValue = formatter
                            .formatCellValue(row.getCell(i))
                            .trim();

                    if (cellValue.isEmpty()) {

                        nullCells++;

                    } else {

                        Double number = tryParseDouble(cellValue);

                        if (number != null) {
                            numericSum += number;
                            numericCount++;
                        }
                    }
                }
            }

            workbook.close();

            fillMetadata(
                    dto,
                    rowCount,
                    columnCount,
                    totalCells,
                    nullCells,
                    numericSum,
                    numericCount,
                    uniqueRows.size()
            );

            return dto;

        } catch (Exception e) {

            throw new ApiException("XLSX analysis failed : " + e.getMessage());
        }
    }

    private void fillMetadata(
            DatasetDto dto,
            int rowCount,
            int columnCount,
            int totalCells,
            int nullCells,
            double numericSum,
            int numericCount,
            int uniqueRowCount
    ) {

        dto.setRecordCount((long) rowCount);
        dto.setColumnCount(columnCount);

        double nullPercentage = totalCells == 0
                ? 0.0
                : (nullCells * 100.0) / totalCells;

        double duplicatePercentage = rowCount == 0
                ? 0.0
                : ((rowCount - uniqueRowCount) * 100.0) / rowCount;

        double value = numericCount == 0
                ? 1.0
                : numericSum / numericCount;

        dto.setNullPercentage(nullPercentage);
        dto.setDuplicatePercentage(duplicatePercentage);
        dto.setValue(value);
    }

    @Override
    public List<DatasetDto> getAllDatasets() {

        List<DatasetEntity> entityList = datasetRepository.findAll();

        return entityList
                .stream()
                .map(appMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DatasetDto getDatasetById(Long id) {

        DatasetEntity entity = datasetRepository.findById(id)
                .orElseThrow(() -> new ApiException("Dataset not found with id : " + id));

        return appMapper.toDto(entity);
    }

    @Override
    public DatasetDto updateDataset(Long id, DatasetDto dto) {

        DatasetEntity entity = datasetRepository.findById(id)
                .orElseThrow(() -> new ApiException("Dataset not found with id : " + id));

        entity.setDatasetName(dto.getDatasetName());
        entity.setOriginalFileName(dto.getOriginalFileName());
        entity.setFilePath(dto.getFilePath());
        entity.setFileType(dto.getFileType());
        entity.setFileSizeBytes(dto.getFileSizeBytes());

        entity.setRecordCount(dto.getRecordCount());
        entity.setColumnCount(dto.getColumnCount());

        entity.setNullPercentage(dto.getNullPercentage());
        entity.setDuplicatePercentage(dto.getDuplicatePercentage());

        double value = dto.getValue() == null ? 0.0 : dto.getValue();

        dto.setValue(value);

        double quantumScore = quantumAnalyzer.calculateQuantumScore(value);

        double sortednessScore = DatasetScoreAlgorithm.calculateSortedness(dto);

        double finalScore = (quantumScore + sortednessScore) / 2.0;

        entity.setValue(value);
        entity.setQuantumScore(quantumScore);
        entity.setSortednessScore(sortednessScore);
        entity.setFinalScore(finalScore);

        DatasetEntity updatedEntity = datasetRepository.save(entity);

        return appMapper.toDto(updatedEntity);
    }

    @Override
    public String deleteDataset(Long id) {

        DatasetEntity entity = datasetRepository.findById(id)
                .orElseThrow(() -> new ApiException("Dataset not found with id : " + id));

        datasetRepository.delete(entity);

        return "Dataset deleted successfully";
    }

    private Double tryParseDouble(String value) {

        try {

            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            return Double.parseDouble(value.replace(",", "").trim());

        } catch (Exception e) {

            return null;
        }
    }

    private String removeExtension(String fileName) {

        if (fileName == null) {
            return "dataset";
        }

        int index = fileName.lastIndexOf(".");

        if (index == -1) {
            return fileName;
        }

        return fileName.substring(0, index);
    }
}