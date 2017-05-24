package com.epsilon.elsautomation.driver;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.epsilon.elsautomation.common.Utility;
import com.epsilon.elsautomation.dto.CycleSuiteExcelDTO;
import com.epsilon.elsautomation.dto.StepExcelDTO;
import com.epsilon.elsautomation.exception.SeleniumFrameworkException;
import com.epsilon.elsautomation.reader.ExcelReader;
import com.epsilon.elsautomation.reader.NotepadReader;
import com.epsilon.elsautomation.selenium.GlobalData;

public class Driver {

	// private static final String TestFile = null;
	// private String testName;

	// Var to store folder location of automation dir
	private final String testLocation;
	// Start File Name Variable
	private final String startFile;

	private final DriverValidator driverValidator;
	private PathHolder pathHolder;

	public Driver(String automationFolderLocation, String startFileName) {
		this.testLocation = Utility.validatePathFormat(automationFolderLocation);
		this.startFile = startFileName;
		driverValidator = new DriverValidator();
	}

	/**
	 * Call this method to execute script. This should be the only entry point
	 * 
	 */
	public void execute() {

		// First Validate given input are valid and all files are present
		boolean allFolderExists = driverValidator.validateFolderExist(testLocation);

		if (!allFolderExists) {
			// Stop Execution
			// Throw Exception
			return;
		}

		// Validate startFile exists
		boolean startFileExist = driverValidator.startFileValidator(testLocation, startFile);
		if (!startFileExist) {
			// Stop Execution
			// Throw Exception
			return;
		}

		initPaths();

		DriverExecutor driverExecutor = new DriverExecutor();
		driverExecutor.runTest(startFile);

	}

	private void initPaths() {
		pathHolder = new PathHolder(testLocation);
		Utility.createLogDirectory(PathHolder.getReportPath());

	}

	public static void main(String[] args) throws FileNotFoundException, SeleniumFrameworkException {

		String testLocation = "C:\\Framework\\";
		String startFileName = "Start.txt";
		String startFilePath = (testLocation + startFileName);

		DriverValidator execute = new DriverValidator();
		// testLocation = validatePathFormat(testLocation);

		boolean b = execute.validateFolderExist(testLocation);

		System.out.println(b);

		boolean b1 = execute.startFileValidator(testLocation, startFileName);

		System.out.println(b1);

		NotepadReader R1 = new NotepadReader();
		List<String> StartFileContent = R1.readNotePad(startFilePath);
		String firstExcelLocation = StartFileContent.get(0);
		// System.out.println(StartFileContent);

		ExcelReader excelReader = new ExcelReader();
		String path = testLocation + "TestFile\\" + firstExcelLocation + ".xlsx";
		int sheetNumber = 0;
		List<CycleSuiteExcelDTO> cycleSuiteExcelDTOs = excelReader.readCycleSuiteExcel(path, sheetNumber);
		System.out.println(cycleSuiteExcelDTOs);

		String path1 = testLocation + "TestFile\\" + firstExcelLocation + ".xlsx";
		int sheetNumber1 = 0;
		List<StepExcelDTO> stepExcelDTOs = excelReader.readStepExcel(path1, sheetNumber1);
		System.out.println(stepExcelDTOs);

	}

}