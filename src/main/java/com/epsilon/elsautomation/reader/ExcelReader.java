package com.epsilon.elsautomation.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.epsilon.elsautomation.dto.CycleSuiteExcelDTO;
import com.epsilon.elsautomation.dto.MappingDTO;
import com.epsilon.elsautomation.dto.StepExcelDTO;
import com.epsilon.elsautomation.exception.SeleniumFrameworkException;
import com.epsilon.elsautomation.selenium.GlobalData;

public class ExcelReader {

	private DecimalFormat decimalFormat;

	public ExcelReader() {
		String pattern = "######.###"; 
		decimalFormat = new DecimalFormat(pattern);
		decimalFormat.setDecimalSeparatorAlwaysShown(false);

		}

	

	public List<CycleSuiteExcelDTO> readCycleSuiteExcel(String path, int sheetNumber) throws SeleniumFrameworkException {
		List<CycleSuiteExcelDTO> excelList = new ArrayList<>();

		FileInputStream inputStream = null;

		Workbook workbook = null;

		try {
			
			inputStream = new FileInputStream(new File(path));

			if (path.endsWith("xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}

			Sheet sheet = workbook.getSheetAt(sheetNumber);

			Iterator<Row> rowIterator = sheet.iterator();

			// First Row is header row, ignore it
			rowIterator.next();

			while (rowIterator.hasNext()) {

				Row currentRow = rowIterator.next();
				CycleSuiteExcelDTO cycleSuiteExcelDTO = new CycleSuiteExcelDTO();

				Cell tableRefCell = currentRow.getCell(0);
				Cell executeCell = currentRow.getCell(1);
				Cell sheetCell = currentRow.getCell(2);
				Cell variableFileCell = currentRow.getCell(3);
				Cell variableNameCell = currentRow.getCell(4);
				Cell purposeCell = currentRow.getCell(5);

				if (tableRefCell != null) {
					cycleSuiteExcelDTO.setTableReference(tableRefCell.getStringCellValue());
				}
				if (executeCell != null) {
					cycleSuiteExcelDTO.setExecute(executeCell.getStringCellValue());
				}
				if (sheetCell != null) {
					int sheetno = 0;
					String sheetNum = sheetCell.getStringCellValue();
					if (StringUtils.isNotBlank(sheetNum)) {
						sheetno = Integer.parseInt(sheetNum);
						cycleSuiteExcelDTO.setSheetToRun(sheetno - 1);
					} else
						sheetno = 0;
				}

				if (variableFileCell != null) {
					cycleSuiteExcelDTO.setVariableFile(variableFileCell.getStringCellValue());
				}
				if (variableNameCell != null) {
					cycleSuiteExcelDTO.setVariableName(variableNameCell.getStringCellValue());
				}
				if (purposeCell != null) {
					cycleSuiteExcelDTO.setPurpose(purposeCell.getStringCellValue());
				}

				excelList.add(cycleSuiteExcelDTO);
			}
		}catch (IOException e) {
			throw new SeleniumFrameworkException("Failed to read cycle/suite Excel File " + path + " sheet : " + sheetNumber
					+ " .Exception is - " + e.getMessage());

		} finally {

			try {
				workbook.close();
				inputStream.close();
			} catch (IOException e) {
				throw new SeleniumFrameworkException("Failed to read cycle/suite Excel File " + path + " sheet : "
						+ sheetNumber + " .Exception is - " + e.getMessage());
			}
		}

		return excelList;

	}

	public List<StepExcelDTO> readStepExcel(String path, int sheetNumber) throws SeleniumFrameworkException {

		List<StepExcelDTO> excelList = new ArrayList<>();
		Workbook workbook = null;
		FileInputStream inputStream = null;

		try {
			inputStream = new FileInputStream(new File(path));

			if (path.endsWith("xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}

			Sheet sheet = workbook.getSheetAt(sheetNumber);

			Iterator<Row> rowIterator = sheet.iterator();

			// First Row is header row, ignore it
			rowIterator.next();

			while (rowIterator.hasNext()) {

				Row currentRow = rowIterator.next();
				StepExcelDTO stepExcelDTO = new StepExcelDTO();

				Cell notesCell = currentRow.getCell(0);

				Cell componentCell = currentRow.getCell(1);
				Cell actionCell = currentRow.getCell(2);
				Cell executeCell = currentRow.getCell(3);
				Cell inputCell = currentRow.getCell(4);
				Cell outputCell = currentRow.getCell(5);
				Cell expectedCell = currentRow.getCell(6);

				if (notesCell != null) {
					stepExcelDTO.setNotes(notesCell.getStringCellValue());
				}

				if (componentCell != null) {
					stepExcelDTO.setComponent(componentCell.getStringCellValue());
				}
				if (actionCell != null) {
					stepExcelDTO.setAction(actionCell.getStringCellValue());
				}
				if (executeCell != null) {
					stepExcelDTO.setExecute(executeCell.getStringCellValue());
				}

				if (inputCell != null) {
					if (inputCell.getCellType() == CellType.STRING.getCode()) {
						stepExcelDTO.setInput(inputCell.getStringCellValue());
					} else if (inputCell.getCellType() == CellType.NUMERIC.getCode()) {
						stepExcelDTO.setInput(decimalFormat.format(inputCell.getNumericCellValue()));
					}

				}
				if (outputCell != null) {
					stepExcelDTO.setOutput(outputCell.getStringCellValue());
				}
				if (expectedCell != null) {
					if (expectedCell.getCellType() == CellType.STRING.getCode()) {
						stepExcelDTO.setExpected(expectedCell.getStringCellValue());
					} else if (expectedCell.getCellType() == CellType.NUMERIC.getCode()) {
						stepExcelDTO.setExpected(String.valueOf(expectedCell.getNumericCellValue()));
					} else if (expectedCell.getCellType() == CellType.BOOLEAN.getCode()) {
						stepExcelDTO.setExpected(String.valueOf(expectedCell.getBooleanCellValue()));
					}

				}

				excelList.add(stepExcelDTO);
			}
		} catch (IOException e) {
			throw new SeleniumFrameworkException("Failed to read Step Excel File " + path + " sheet : " + sheetNumber
					+ " .Exception is - " + e.getMessage());

		} finally {

			try {
				workbook.close();
				inputStream.close();
			} catch (IOException e) {
				throw new SeleniumFrameworkException("Failed to read Step Excel File " + path + " sheet : "
						+ sheetNumber + " .Exception is - " + e.getMessage());
			}
		}

		return excelList;

	}

	public MappingDTO searchMappingFile(String mappingPath, int sheetNumber, String component)
			throws SeleniumFrameworkException {

		FileInputStream inputStream = null;
		Workbook workbook = null;

		try {
			inputStream = new FileInputStream(new File(mappingPath));

			if (mappingPath.endsWith("xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}

			Sheet sheet = workbook.getSheetAt(sheetNumber);

			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {

				Row currentRow = rowIterator.next();

				Cell componentName = currentRow.getCell(1);

				if (componentName != null && componentName.getStringCellValue().equalsIgnoreCase(component)) {

					MappingDTO mappingDTO = new MappingDTO();

					Cell propertyName = currentRow.getCell(2);
					Cell propertyValue = currentRow.getCell(3);

					mappingDTO.setComponenet(componentName.getStringCellValue());

					if (propertyName != null) {
						mappingDTO.setPropertyName(propertyName.getStringCellValue());
					}
					if (propertyValue != null) {
						mappingDTO.setPropertyValue(propertyValue.getStringCellValue());
					}
					return mappingDTO;
				}

			}
		} catch (FileNotFoundException e) {
			throw new SeleniumFrameworkException(
					"Mapping file " + mappingPath + " sheet number " + sheetNumber + " not found");
		} catch (IOException e) {
			throw new SeleniumFrameworkException("IO Exception occured while searching in mapping file " + mappingPath);
		}

		finally {

			try {
				workbook.close();
				inputStream.close();
			} catch (IOException e) {
				throw new SeleniumFrameworkException("IO Exception occured while searching in mapping file");
			}
		}
		return null;

	}


	public String[][] readDataList(int sheetno, String VariableFile, String VariableName)
			throws SeleniumFrameworkException {

		FileInputStream inputStream = null;
		Workbook workbook = null;
		try {
			inputStream = new FileInputStream(new File(VariableFile));

			if (VariableFile.endsWith("xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}

			Sheet sheet = workbook.getSheetAt(sheetno);

			int rows = sheet.getPhysicalNumberOfRows();

			int column = sheet.getRow(0).getPhysicalNumberOfCells();

			String[][] data = new String[rows - 1][column];

			for (int r = 0; r < rows - 1; r++) {
				for (int c = 0; c < column; c++) {
					Cell curCell = sheet.getRow(r + 1).getCell(c);
					if (curCell!= null){
					String curCellValue = null;
					switch (curCell.getCellTypeEnum()) {

					case STRING:
						curCellValue = curCell.getStringCellValue();
						break;
					case NUMERIC:
						double n = curCell.getNumericCellValue();

						curCellValue = decimalFormat.format(n);
						break;

					case BOOLEAN:
						boolean b = curCell.getBooleanCellValue();
						curCellValue = String.valueOf(b);

					default:
						break;
					}
					
					data[r][c] = curCellValue;
					}
				}
			}

			GlobalData.dataMap.put(VariableName, data);
			return data;

		} catch (IOException e) {
			throw new SeleniumFrameworkException("Failed to read data file " + VariableFile + " sheet no: " + sheetno
					+ " . Exception is - " + e.getMessage());
		} finally {
			try {
				workbook.close();
				inputStream.close();
			} catch (IOException e) {
				throw new SeleniumFrameworkException("Failed to read data file " + VariableFile + " sheet no: "
						+ sheetno + " . Exception is - " + e.getMessage());
			}

		}

	}

	public void readGlobalSheet(String fileLocation, int sheetNumber) throws FileNotFoundException {

		FileInputStream inputStream = new FileInputStream(new File(fileLocation));

		Workbook workbook = null;

		try {

			if (fileLocation.endsWith("xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}

			Sheet sheet = workbook.getSheetAt(sheetNumber);

			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {

				Row currentRow = rowIterator.next();
				Cell name = currentRow.getCell(1);
				Cell value = currentRow.getCell(2);
				String name1 = name.getStringCellValue();
				String value1 = value.getStringCellValue();

				if (name != null && value != null) {
					GlobalData.globalSheetMap.put(name1.toLowerCase(), value1.toLowerCase());
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
