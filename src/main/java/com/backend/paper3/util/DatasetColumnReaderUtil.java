package com.backend.paper3.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.backend.paper3.exception.ApiException;

public class DatasetColumnReaderUtil {

    private static final int MAX_VALUES_FOR_NOW = 100000;

    public static List<String> readColumnValues(
            String filePath,
            String fileType,
            String selectedColumn
    ) {

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ApiException("Dataset file path is missing");
        }

        Path path =
                Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new ApiException("Dataset file not found in storage");
        }

        if (fileType == null || fileType.trim().isEmpty()) {
            throw new ApiException("Dataset file type is missing");
        }

        if (fileType.equalsIgnoreCase("CSV")) {
            return readCsvColumn(
                    filePath,
                    selectedColumn
            );
        }

        if (fileType.equalsIgnoreCase("XLSX")) {
            return readXlsxColumn(
                    filePath,
                    selectedColumn
            );
        }

        throw new ApiException(
                "Sorting supported only for CSV and XLSX files"
        );
    }

    private static List<String> readCsvColumn(
            String filePath,
            String selectedColumn
    ) {

        try {

            List<String> values =
                    new ArrayList<>();

            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(filePath)
                            )
                    );

            String headerLine =
                    br.readLine();

            if (headerLine == null || headerLine.trim().isEmpty()) {
                br.close();
                return values;
            }

            String[] headers =
                    headerLine.split(",", -1);

            int columnIndex =
                    resolveColumnIndex(
                            headers,
                            selectedColumn
                    );

            String line;

            while ((line = br.readLine()) != null) {

                String[] cells =
                        line.split(",", -1);

                if (columnIndex < cells.length) {

                    String value =
                            cells[columnIndex] == null
                                    ? ""
                                    : cells[columnIndex].trim();

                    if (!value.isEmpty()) {
                        values.add(value);
                    }
                }

                if (values.size() >= MAX_VALUES_FOR_NOW) {
                    break;
                }
            }

            br.close();

            return values;

        } catch (ApiException e) {

            throw e;

        } catch (Exception e) {

            throw new ApiException(
                    "CSV column read failed : " + e.getMessage()
            );
        }
    }

    private static List<String> readXlsxColumn(
            String filePath,
            String selectedColumn
    ) {

        try {

            List<String> values =
                    new ArrayList<>();

            Workbook workbook =
                    new XSSFWorkbook(
                            new FileInputStream(filePath)
                    );

            Sheet sheet =
                    workbook.getSheetAt(0);

            DataFormatter formatter =
                    new DataFormatter();

            Row headerRow =
                    sheet.getRow(0);

            if (headerRow == null) {
                workbook.close();
                return values;
            }

            int lastCellNumber =
                    headerRow.getLastCellNum();

            if (lastCellNumber < 0) {
                workbook.close();
                return values;
            }

            String[] headers =
                    new String[lastCellNumber];

            for (int i = 0; i < lastCellNumber; i++) {

                headers[i] =
                        formatter
                                .formatCellValue(
                                        headerRow.getCell(i)
                                )
                                .trim();
            }

            int columnIndex =
                    resolveColumnIndex(
                            headers,
                            selectedColumn
                    );

            int physicalRows =
                    sheet.getPhysicalNumberOfRows();

            for (int rowIndex = 1; rowIndex < physicalRows; rowIndex++) {

                Row row =
                        sheet.getRow(rowIndex);

                if (row == null) {
                    continue;
                }

                String value =
                        formatter
                                .formatCellValue(
                                        row.getCell(columnIndex)
                                )
                                .trim();

                if (!value.isEmpty()) {
                    values.add(value);
                }

                if (values.size() >= MAX_VALUES_FOR_NOW) {
                    break;
                }
            }

            workbook.close();

            return values;

        } catch (ApiException e) {

            throw e;

        } catch (Exception e) {

            throw new ApiException(
                    "XLSX column read failed : " + e.getMessage()
            );
        }
    }

    private static int resolveColumnIndex(
            String[] headers,
            String selectedColumn
    ) {

        if (headers == null || headers.length == 0) {
            return 0;
        }

        if (selectedColumn == null
                || selectedColumn.trim().isEmpty()
                || selectedColumn.equalsIgnoreCase("AUTO_DETECTED_FIRST_COLUMN")) {

            return 0;
        }

        String requestedColumn =
                selectedColumn.trim();

        for (int i = 0; i < headers.length; i++) {

            String header =
                    headers[i] == null
                            ? ""
                            : headers[i].trim();

            if (header.equalsIgnoreCase(requestedColumn)) {
                return i;
            }
        }

        throw new ApiException(
                "Selected column not found in dataset : " + selectedColumn
        );
    }
}