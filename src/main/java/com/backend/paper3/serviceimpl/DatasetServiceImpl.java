package com.backend.paper3.serviceimpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.backend.paper3.dto.DatasetPreviewDto;
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
import com.backend.paper3.enums.DatasetPattern;
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
	private DatasetMapper datasetMapper;

	@Autowired
	private QuantumDatasetAnalyzer quantumAnalyzer;

	@Autowired
	private LocalDatasetStorageService localDatasetStorageService;

	@Override
	public DatasetDto createDataset(DatasetDto dto) {

		try {

			validateDatasetDto(dto);

			normalizeDtoDefaults(dto);

			double value = dto.getValue() == null ? 0.0 : dto.getValue();

			dto.setValue(value);

			double quantumScore = quantumAnalyzer.calculateQuantumScore(value);

			double sortednessScore = DatasetScoreAlgorithm.calculateSortedness(dto);

			double finalScore = (quantumScore + sortednessScore) / 2.0;

			dto.setQuantumScore(quantumScore);
			dto.setSortednessScore(sortednessScore);
			dto.setFinalScore(finalScore);

			DatasetEntity entity = new DatasetEntity();

			copyDtoToEntity(dto, entity, true);

			DatasetEntity savedEntity = datasetRepository.save(entity);

			return datasetMapper.toDto(savedEntity);

		} catch (ApiException e) {

			throw e;

		} catch (Exception e) {

			throw new ApiException("Failed to create dataset : " + e.getMessage());
		}
	}

	@Override
	public DatasetDto createFromCsv(MultipartFile file) {

		try {

			validateFile(file);

			String originalFileName = file.getOriginalFilename();

			Optional<DatasetEntity> existingDataset = datasetRepository
					.findByOriginalFileNameAndFileSizeBytes(originalFileName, file.getSize());

			if (existingDataset.isPresent()) {
				return datasetMapper.toDto(existingDataset.get());
			}

			String storedPath = localDatasetStorageService.storeFile(file);

			DatasetDto dto = fastReadCsvHeader(file);

			String datasetName = removeExtension(originalFileName);

			dto.setDatasetName(datasetName);

			dto.setDatasetUniqueId(generateUniqueDatasetId(datasetName));

			dto.setOriginalFileName(originalFileName);
			dto.setFilePath(storedPath);
			dto.setFileType("CSV");
			dto.setFileSizeBytes(file.getSize());

			return createDataset(dto);

		} catch (ApiException e) {

			throw e;

		} catch (Exception e) {

			throw new ApiException("CSV upload failed : " + e.getMessage());
		}
	}

	@Override
	public DatasetDto createFromXlsx(MultipartFile file) {

		try {

			validateFile(file);

			String originalFileName = file.getOriginalFilename();

			Optional<DatasetEntity> existingDataset = datasetRepository
					.findByOriginalFileNameAndFileSizeBytes(originalFileName, file.getSize());

			if (existingDataset.isPresent()) {
				return datasetMapper.toDto(existingDataset.get());
			}

			String storedPath = localDatasetStorageService.storeFile(file);

			DatasetDto dto = fastReadXlsxHeader(file);

			String datasetName = removeExtension(originalFileName);

			dto.setDatasetName(datasetName);

			dto.setDatasetUniqueId(generateUniqueDatasetId(datasetName));

			dto.setOriginalFileName(originalFileName);
			dto.setFilePath(storedPath);
			dto.setFileType("XLSX");
			dto.setFileSizeBytes(file.getSize());

			return createDataset(dto);

		} catch (ApiException e) {

			throw e;

		} catch (Exception e) {

			throw new ApiException("XLSX upload failed : " + e.getMessage());
		}
	}

	@Override
	public List<DatasetDto> getAllDatasets() {

		List<DatasetEntity> entityList = datasetRepository.findAll();

		return entityList.stream().map(datasetMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public DatasetDto getDatasetById(Long id) {

		DatasetEntity entity = datasetRepository.findById(id)
				.orElseThrow(() -> new ApiException("Dataset not found with id : " + id));

		return datasetMapper.toDto(entity);
	}

	@Override
	public DatasetDto getDatasetByUniqueId(String datasetUniqueId) {

		if (datasetUniqueId == null || datasetUniqueId.trim().isEmpty()) {

			throw new ApiException("Dataset unique id is required");
		}

		DatasetEntity entity = datasetRepository.findByDatasetUniqueId(datasetUniqueId.trim())
				.orElseThrow(() -> new ApiException("Dataset not found with unique id : " + datasetUniqueId));

		return datasetMapper.toDto(entity);
	}

	@Override
	public DatasetDto updateDataset(Long id, DatasetDto dto) {

		try {

			if (dto == null) {
				throw new ApiException("Dataset payload is null");
			}

			DatasetEntity entity = datasetRepository.findById(id)
					.orElseThrow(() -> new ApiException("Dataset not found with id : " + id));

			if (dto.getDatasetName() == null || dto.getDatasetName().trim().isEmpty()) {

				dto.setDatasetName(entity.getDatasetName());
			}

			if (dto.getDatasetUniqueId() == null || dto.getDatasetUniqueId().trim().isEmpty()) {

				dto.setDatasetUniqueId(entity.getDatasetUniqueId());
			}

			normalizeDtoDefaults(dto);

			double value = dto.getValue() == null ? 0.0 : dto.getValue();

			dto.setValue(value);

			double quantumScore = quantumAnalyzer.calculateQuantumScore(value);

			double sortednessScore = DatasetScoreAlgorithm.calculateSortedness(dto);

			double finalScore = (quantumScore + sortednessScore) / 2.0;

			dto.setQuantumScore(quantumScore);
			dto.setSortednessScore(sortednessScore);
			dto.setFinalScore(finalScore);

			copyDtoToEntity(dto, entity, false);

			DatasetEntity updatedEntity = datasetRepository.save(entity);

			return datasetMapper.toDto(updatedEntity);

		} catch (ApiException e) {

			throw e;

		} catch (Exception e) {

			throw new ApiException("Failed to update dataset : " + e.getMessage());
		}
	}

	@Override
	public String deleteDataset(Long id) {

		DatasetEntity entity = datasetRepository.findById(id)
				.orElseThrow(() -> new ApiException("Dataset not found with id : " + id));

		datasetRepository.delete(entity);

		return "Dataset deleted successfully";
	}

	private DatasetDto fastReadCsvHeader(MultipartFile file) {

		try {

			DatasetDto dto = new DatasetDto();

			BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));

			String headerLine = br.readLine();

			br.close();

			if (headerLine == null || headerLine.trim().isEmpty()) {

				dto.setColumnCount(0);
				dto.setSelectedSortColumn("AUTO_DETECTED_FIRST_COLUMN");

			} else {

				String[] headers = headerLine.split(",", -1);

				dto.setColumnCount(headers.length);

				if (headers.length > 0 && headers[0] != null && !headers[0].trim().isEmpty()) {

					dto.setSelectedSortColumn(headers[0].trim());

				} else {

					dto.setSelectedSortColumn("AUTO_DETECTED_FIRST_COLUMN");
				}
			}

			dto.setRecordCount(0L);
			dto.setNullPercentage(0.0);
			dto.setDuplicatePercentage(0.0);
			dto.setSkewnessValue(0.0);
			dto.setDetectedPattern(DatasetPattern.UNKNOWN);
			dto.setDataType("PENDING_ANALYSIS");
			dto.setValue(0.0);

			return dto;

		} catch (Exception e) {

			throw new ApiException("CSV fast header read failed : " + e.getMessage());
		}
	}

	private DatasetDto fastReadXlsxHeader(MultipartFile file) {

		try {

			DatasetDto dto = new DatasetDto();

			Workbook workbook = new XSSFWorkbook(file.getInputStream());

			Sheet sheet = workbook.getSheetAt(0);

			DataFormatter formatter = new DataFormatter();

			int physicalRows = sheet.getPhysicalNumberOfRows();

			Row headerRow = sheet.getRow(0);

			if (headerRow == null) {

				dto.setColumnCount(0);
				dto.setSelectedSortColumn("AUTO_DETECTED_FIRST_COLUMN");

			} else {

				int columnCount = headerRow.getLastCellNum();

				if (columnCount < 0) {
					columnCount = 0;
				}

				dto.setColumnCount(columnCount);

				if (columnCount > 0) {

					String firstHeader = formatter.formatCellValue(headerRow.getCell(0)).trim();

					if (firstHeader.isEmpty()) {

						dto.setSelectedSortColumn("AUTO_DETECTED_FIRST_COLUMN");

					} else {

						dto.setSelectedSortColumn(firstHeader);
					}

				} else {

					dto.setSelectedSortColumn("AUTO_DETECTED_FIRST_COLUMN");
				}
			}

			workbook.close();

			long estimatedRows = physicalRows <= 0 ? 0L : physicalRows - 1L;

			dto.setRecordCount(estimatedRows);

			dto.setNullPercentage(0.0);
			dto.setDuplicatePercentage(0.0);
			dto.setSkewnessValue(0.0);
			dto.setDetectedPattern(DatasetPattern.UNKNOWN);
			dto.setDataType("PENDING_ANALYSIS");
			dto.setValue(0.0);

			return dto;

		} catch (Exception e) {

			throw new ApiException("XLSX fast header read failed : " + e.getMessage());
		}
	}

	private void validateDatasetDto(DatasetDto dto) {

		if (dto == null) {
			throw new ApiException("Dataset payload is null");
		}

		if (dto.getDatasetName() == null || dto.getDatasetName().trim().isEmpty()) {

			throw new ApiException("Dataset name is required");
		}
	}

	private void validateFile(MultipartFile file) {

		if (file == null || file.isEmpty()) {
			throw new ApiException("Uploaded file is empty");
		}

		String originalFileName = file.getOriginalFilename();

		if (originalFileName == null || originalFileName.isBlank()) {

			throw new ApiException("Invalid file name");
		}
	}private DatasetPreviewDto basePreview(
	        DatasetEntity entity
			) {

			    DatasetPreviewDto preview =
			            new DatasetPreviewDto();

			    preview.setDatasetId(
			            entity.getId()
			    );

			    preview.setDatasetUniqueId(
			            entity.getDatasetUniqueId()
			    );

			    preview.setDatasetName(
			            entity.getDatasetName()
			    );

			    preview.setFileType(
			            entity.getFileType()
			    );

			    return preview;
			}
	private DatasetPreviewDto previewCsv(
	        DatasetEntity entity
	) {

	    try {

	        DatasetPreviewDto preview =
	                basePreview(entity);

	        List<String> columns =
	                new ArrayList<>();

	        List<List<String>> rows =
	                new ArrayList<>();

	        BufferedReader br =
	                new BufferedReader(
	                        new InputStreamReader(
	                                new FileInputStream(
	                                        entity.getFilePath()
	                                )
	                        )
	                );

	        String line;
	        int lineNumber = 0;

	        while ((line = br.readLine()) != null) {

	            String[] cells =
	                    line.split(",", -1);

	            if (lineNumber == 0) {

	                for (String cell : cells) {
	                    columns.add(
	                            cell == null
	                                    ? ""
	                                    : cell.trim()
	                    );
	                }

	            } else {

	                List<String> row =
	                        new ArrayList<>();

	                for (String cell : cells) {
	                    row.add(
	                            cell == null
	                                    ? ""
	                                    : cell.trim()
	                    );
	                }

	                rows.add(row);
	            }

	            lineNumber++;

	            if (lineNumber > 10) {
	                break;
	            }
	        }

	        br.close();

	        preview.setColumns(columns);
	        preview.setRows(rows);

	        return preview;

	    } catch (Exception e) {

	        throw new ApiException(
	                "CSV preview failed : " + e.getMessage()
	        );
	    }
	}private DatasetPreviewDto previewXlsx(
	        DatasetEntity entity
			) {

			    try {

			        DatasetPreviewDto preview =
			                basePreview(entity);

			        List<String> columns =
			                new ArrayList<>();

			        List<List<String>> rows =
			                new ArrayList<>();

			        Workbook workbook =
			                new XSSFWorkbook(
			                        new FileInputStream(
			                                entity.getFilePath()
			                        )
			                );

			        Sheet sheet =
			                workbook.getSheetAt(0);

			        DataFormatter formatter =
			                new DataFormatter();

			        int maxRows =
			                Math.min(
			                        sheet.getPhysicalNumberOfRows(),
			                        11
			                );

			        for (int rowIndex = 0; rowIndex < maxRows; rowIndex++) {

			            Row row =
			                    sheet.getRow(rowIndex);

			            if (row == null) {
			                continue;
			            }

			            int lastCellNumber =
			                    row.getLastCellNum();

			            if (lastCellNumber < 0) {
			                continue;
			            }

			            if (rowIndex == 0) {

			                for (int cellIndex = 0; cellIndex < lastCellNumber; cellIndex++) {

			                    columns.add(
			                            formatter
			                                    .formatCellValue(
			                                            row.getCell(cellIndex)
			                                    )
			                                    .trim()
			                    );
			                }

			            } else {

			                List<String> rowValues =
			                        new ArrayList<>();

			                for (int cellIndex = 0; cellIndex < lastCellNumber; cellIndex++) {

			                    rowValues.add(
			                            formatter
			                                    .formatCellValue(
			                                            row.getCell(cellIndex)
			                                    )
			                                    .trim()
			                    );
			                }

			                rows.add(rowValues);
			            }
			        }

			        workbook.close();

			        preview.setColumns(columns);
			        preview.setRows(rows);

			        return preview;

			    } catch (Exception e) {

			        throw new ApiException(
			                "XLSX preview failed : " + e.getMessage()
			        );
			    }
			}

	private void normalizeDtoDefaults(DatasetDto dto) {

		if (dto.getDatasetUniqueId() == null || dto.getDatasetUniqueId().trim().isEmpty()) {

			dto.setDatasetUniqueId(generateUniqueDatasetId(dto.getDatasetName()));
		}

		if (dto.getNullPercentage() == null) {
			dto.setNullPercentage(0.0);
		}

		if (dto.getDuplicatePercentage() == null) {
			dto.setDuplicatePercentage(0.0);
		}

		if (dto.getSkewnessValue() == null) {
			dto.setSkewnessValue(0.0);
		}

		if (dto.getDetectedPattern() == null) {
			dto.setDetectedPattern(DatasetPattern.UNKNOWN);
		}

		if (dto.getDataType() == null || dto.getDataType().trim().isEmpty()) {

			dto.setDataType("PENDING_ANALYSIS");
		}

		if (dto.getSelectedSortColumn() == null || dto.getSelectedSortColumn().trim().isEmpty()) {

			dto.setSelectedSortColumn("AUTO_DETECTED_FIRST_COLUMN");
		}

		if (dto.getValue() == null) {
			dto.setValue(0.0);
		}

		if (dto.getRecordCount() == null) {
			dto.setRecordCount(0L);
		}

		if (dto.getColumnCount() == null) {
			dto.setColumnCount(0);
		}
	}

	private void copyDtoToEntity(DatasetDto dto, DatasetEntity entity, boolean isCreate) {

		entity.setDatasetUniqueId(dto.getDatasetUniqueId());

		entity.setDatasetName(dto.getDatasetName());

		entity.setOriginalFileName(dto.getOriginalFileName());

		entity.setFilePath(dto.getFilePath());

		entity.setFileType(dto.getFileType());

		entity.setFileSizeBytes(dto.getFileSizeBytes());

		entity.setRecordCount(dto.getRecordCount());

		entity.setColumnCount(dto.getColumnCount());

		entity.setSelectedSortColumn(dto.getSelectedSortColumn());

		entity.setDataType(dto.getDataType());

		entity.setDetectedPattern(dto.getDetectedPattern());

		entity.setValue(dto.getValue());

		entity.setDuplicatePercentage(dto.getDuplicatePercentage());

		entity.setNullPercentage(dto.getNullPercentage());

		entity.setSkewnessValue(dto.getSkewnessValue());

		entity.setSortednessScore(dto.getSortednessScore());

		entity.setQuantumScore(dto.getQuantumScore());

		entity.setFinalScore(dto.getFinalScore());

		if (isCreate) {
			entity.setCreatedAt(LocalDateTime.now());
		}
	}

	private String generateUniqueDatasetId(String datasetName) {

		String safeName = datasetName == null ? "DATASET"
				: datasetName.trim().toUpperCase().replaceAll("[^A-Z0-9]", "_").replaceAll("_+", "_");

		if (safeName.isBlank()) {
			safeName = "DATASET";
		}

		if (safeName.length() > 20) {
			safeName = safeName.substring(0, 20);
		}

		String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		String datasetUniqueId;

		do {

			String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

			datasetUniqueId = "DS-" + safeName + "-" + dateTime + "-" + random;

		} while (datasetRepository.existsByDatasetUniqueId(datasetUniqueId));

		return datasetUniqueId;
	}

	private String removeExtension(String fileName) {

		if (fileName == null || fileName.isBlank()) {

			return "dataset";
		}

		int index = fileName.lastIndexOf(".");

		if (index == -1) {
			return fileName;
		}

		return fileName.substring(0, index);
	}
	@Override
	public DatasetPreviewDto previewDataset(
	        Long id
	) {

	    DatasetEntity entity =
	            datasetRepository
	                    .findById(id)
	                    .orElseThrow(
	                            () -> new ApiException(
	                                    "Dataset not found with id : " + id
	                            )
	                    );

	    if (entity.getFilePath() == null
	            || entity.getFilePath().trim().isEmpty()) {

	        throw new ApiException(
	                "Dataset file path is missing"
	        );
	    }

	    Path path =
	            Paths.get(entity.getFilePath());

	    if (!Files.exists(path)) {
	        throw new ApiException(
	                "Dataset file not found in storage"
	        );
	    }

	    String fileType =
	            entity.getFileType();

	    if (fileType == null) {
	        throw new ApiException(
	                "Dataset file type is missing"
	        );
	    }

	    if (fileType.equalsIgnoreCase("CSV")) {
	        return previewCsv(entity);
	    }

	    if (fileType.equalsIgnoreCase("XLSX")) {
	        return previewXlsx(entity);
	    }

	    throw new ApiException(
	            "Preview supported only for CSV and XLSX"
	    );
	}
}