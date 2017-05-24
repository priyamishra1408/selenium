package com.epsilon.elsautomation.driver;

import java.io.FileNotFoundException;

import org.apache.commons.lang3.StringUtils;

import com.epsilon.elsautomation.common.Utility;
import com.epsilon.elsautomation.reader.ExcelReader;
import com.epsilon.elsautomation.selenium.GlobalData;

public class PathHolder {

	private static String testFiles;

	private static String dataFiles;
	
	private static String globalDataFile;

	private static String mappingFiles;

	private static String mainFolder;
	
	private static String reportPath;
	
	private static String cycleReportpath;
	
	private static String suiteReportpath;
	
	private static String stepReportpath;
	
	private ExcelReader excelReader;
	

	PathHolder(String testLocation) {
		mainFolder = testLocation;
		testFiles = testLocation + "TestFile\\";
		dataFiles = testLocation + "DataFile\\";
		globalDataFile = testLocation + "DataFile\\GlobalVariables.xls";
		mappingFiles = testLocation + "Mapping\\ELSMappingPriya.xls";
		
		excelReader = new ExcelReader();
		try {
			excelReader.readGlobalSheet(PathHolder.getGLobalDataFile(), 0);
		} catch (FileNotFoundException e) {
			System.out.println(
					"Global Data sheet not found. Please create GlobalVariables.xls file under DataFile directory.");
		}

		reportPath = GlobalData.globalSheetMap.get("report");

		if (StringUtils.isBlank(reportPath)) {
			reportPath = "C:\\tmp\\";
		}
		
		reportPath = Utility.validatePathFormat(reportPath);
		cycleReportpath = reportPath+"cycleReport\\\\";
		suiteReportpath = reportPath+"suiteReport\\\\";
		stepReportpath = reportPath+"stepReport\\\\";
	}

	public static String getMainFolder() {
		return mainFolder;
	}

	public static String getTestFiles() {
		return testFiles;
	}

	public static String getDataFiles() {
		return dataFiles;
	}

	public static String getMappingFiles() {
		return mappingFiles;
	}
	
	public static String getGLobalDataFile() {
		return globalDataFile;
	}

	public static String getReportPath() {
		return reportPath;
	}

	public static String getCycleReportpath() {
		return cycleReportpath;
	}

	public static String getSuiteReportpath() {
		return suiteReportpath;
	}

	public static String getStepReportpath() {
		return stepReportpath;
	}
	
	

}
