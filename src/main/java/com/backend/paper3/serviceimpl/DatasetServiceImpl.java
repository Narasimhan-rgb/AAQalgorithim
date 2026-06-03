package com.backend.paper3.serviceimpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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

			rejectDuplicateUpload(originalFileName, file.getSize());

			String storedPath = localDatasetStorageService.storeFile(file);

			DatasetDto dto = analyzeCsv(file);

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

			rejectDuplicateUpload(originalFileName, file.getSize());

			String storedPath = localDatasetStorageService.storeFile(file);

			DatasetDto dto = analyzeXlsx(file);

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

	private DatasetDto analyzeCsv(MultipartFile file) {

		try {

			DatasetDto dto = new DatasetDto();

			BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));

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

					if (cells.length > 0) {
						dto.setSelectedSortColumn(cleanText(cells[0]));
					}

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

			fillMetadata(dto, rowCount, columnCount, totalCells, nullCells, numericSum, numericCount,
					uniqueRows.size());

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

					if (lastCellNumber > 0) {

						String firstHeader = formatter.formatCellValue(row.getCell(0)).trim();

						dto.setSelectedSortColumn(cleanText(firstHeader));
					}

					headerSkipped = true;
					continue;
				}

				StringBuilder rowData = new StringBuilder();

				boolean emptyRow = true;

				for (int i = 0; i < lastCellNumber; i++) {

					String cellValue = formatter.formatCellValue(row.getCell(i)).trim();

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

					String cellValue = formatter.formatCellValue(row.getCell(i)).trim();

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

			fillMetadata(dto, rowCount, columnCount, totalCells, nullCells, numericSum, numericCount,
					uniqueRows.size());

			return dto;

		} catch (Exception e) {

			throw new ApiException("XLSX analysis failed : " + e.getMessage());
		}
	}

	private void fillMetadata(DatasetDto dto, int rowCount, int columnCount, int totalCells, int nullCells,
			double numericSum, int numericCount, int uniqueRowCount) {

		dto.setRecordCount((long) rowCount);

		dto.setColumnCount(columnCount);

		double nullPercentage = totalCells == 0 ? 0.0 : (nullCells * 100.0) / totalCells;

		double duplicatePercentage = rowCount == 0 ? 0.0 : ((rowCount - uniqueRowCount) * 100.0) / rowCount;

		double value = numericCount == 0 ? 1.0 : numericSum / numericCount;

		dto.setNullPercentage(nullPercentage);

		dto.setDuplicatePercentage(duplicatePercentage);

		dto.setValue(value);

		dto.setDataType(numericCount > 0 ? "NUMERIC" : "TEXT");

		dto.setSkewnessValue(0.0);

		dto.setDetectedPattern(detectPattern(duplicatePercentage));
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
	}

	private void rejectDuplicateUpload(String originalFileName, Long fileSizeBytes) {

		if (originalFileName == null || fileSizeBytes == null) {
			return;
		}

		boolean alreadyExists = datasetRepository.existsByOriginalFileNameAndFileSizeBytes(originalFileName,
				fileSizeBytes);

		if (alreadyExists) {
			throw new ApiException("This dataset file is already uploaded. Duplicate insert stopped.");
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

			dto.setDataType("UNKNOWN");
		}

		if (dto.getSelectedSortColumn() == null || dto.getSelectedSortColumn().trim().isEmpty()) {

			dto.setSelectedSortColumn("AUTO_DETECTED_FIRST_COLUMN");
		}

		if (dto.getValue() == null) {
			dto.setValue(0.0);
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

	private DatasetPattern detectPattern(double duplicatePercentage) {

		if (duplicatePercentage >= 30.0) {
			return DatasetPattern.REPEATED_VALUES;
		}

		return DatasetPattern.UNKNOWN;
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

	private String cleanText(String value) {

		if (value == null) {
			return null;
		}

		String trimmed = value.trim();

		if (trimmed.isEmpty()) {
			return null;
		}

		return trimmed;
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
}