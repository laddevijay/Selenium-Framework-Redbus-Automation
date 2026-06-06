package com.redbus.utils;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ExcelUtil {

	private static final Logger logger = LogManager.getLogger(ExcelUtil.class);

	private ExcelUtil() {
	}

	// -------------------- Read Excel as List<Map<Header, Value>>

	public static List<Map<String, String>> readExcel(String filePath, String sheetName) {
		List<Map<String, String>> data = new ArrayList<>();
		try (
				FileInputStream fis = new FileInputStream(filePath); 
				Workbook workbook = new XSSFWorkbook(fis)
			){
			
			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				throw new IllegalArgumentException("Sheet not found: " + sheetName);
			}

			Row headerRow = sheet.getRow(0);

			if (headerRow == null) {
				throw new RuntimeException("Header row is missing in sheet: " + sheetName);
			}

			int rowCount = sheet.getPhysicalNumberOfRows();
			int colCount = headerRow.getPhysicalNumberOfCells();

			for (int i = 1; i < rowCount; i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;
				Map<String, String> rowData = new LinkedHashMap<>();
				for (int j = 0; j < colCount; j++) {
					Cell headerCell = headerRow.getCell(j);
					if (headerCell == null)
						continue;
					String key = headerCell.getStringCellValue();
					Cell cell = row.getCell(j);
					String value = (cell == null) ? "" : getCellValue(cell);
					rowData.put(key, value);
				}
				data.add(rowData);
			}
		} catch (Exception e) {
			logger.error("Error reading Excel file", e);
			throw new RuntimeException(e);
		}
		return data;
	}

	// -------------------- DataProvider Support --------------------

	public static Object[][] getData(String filePath, String sheetName) {
		List<Map<String, String>> list = readExcel(filePath, sheetName);
		Object[][] data = new Object[list.size()][1];
		for (int i = 0; i < list.size(); i++) {
			data[i][0] = list.get(i);
		}
		return data;
	}

	// -------------------- Write Back to Excel --------------------

	public static void writeCellValue(String filePath, String sheetName, int rowNum, String columnName, String value) {

		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				throw new RuntimeException("Sheet not found: " + sheetName);
			}

			Row headerRow = sheet.getRow(0);

			if (headerRow == null) {
				throw new RuntimeException("Header row is missing in sheet: " + sheetName);
			}

			int colIndex = getColumnIndex(headerRow, columnName);

			if (colIndex == -1) {
				throw new RuntimeException("Column not found: " + columnName);
			}

			Row row = sheet.getRow(rowNum);
			if (row == null)
				row = sheet.createRow(rowNum);

			Cell cell = row.getCell(colIndex);
			if (cell == null)
				cell = row.createCell(colIndex);

			cell.setCellValue(value);

			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				workbook.write(fos);
			}

			logger.info("Written [{}] at row {} column {}", value, rowNum, columnName);

		} catch (Exception e) {
			logger.error("Error writing to Excel", e);
			throw new RuntimeException(e);
		}
	}

	// -------------------- Helper --------------------

	private static int getColumnIndex(Row headerRow, String columnName) {
		for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
			Cell cell = headerRow.getCell(i);
			if (cell != null && cell.getStringCellValue().equalsIgnoreCase(columnName)) {
				return i;
			}
		}
		return -1;
	}

	private static String getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			}
			return String.valueOf((long) cell.getNumericCellValue());
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		default:
			return "";
		}
	}
}