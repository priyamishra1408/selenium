package com.epsilon.elsautomation.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.epsilon.elsautomation.dto.StepExcelDTO;
import com.epsilon.elsautomation.exception.SeleniumFrameworkException;

public class Utility {
	
	
	/**
	 * Check if path ends wit /,and add / if it is not found
	 * 
	 * @param testLocation
	 * @return
	 */
	public static String validatePathFormat(String testLocation) {
		
		if (!testLocation.endsWith("\\")) {
			String newTestLocation = testLocation + "\\\\";
			return newTestLocation;

		}
		return testLocation;
	}

	/**
	 * Split string based in ( to get name and sheet number
	 * 
	 * @param fileName
	 * @return List of string, first element is name and 2nd element is number
	 */
	public static List<String> getExcelNameSheet(String fileName) {
		if (fileName != null) {
			String[] strArray = fileName.split("\\(");
			String name = strArray[0];
			int sheetNum = 0;

			if (strArray.length > 1) {
				String sheetStr = strArray[1].replace(")", "");
				sheetNum = Integer.parseInt(sheetStr);
				sheetNum = sheetNum - 1;
			}

			List<String> list = new ArrayList<>();
			list.add(name);
			list.add(String.valueOf(sheetNum));

			return list;

		} else {
			return null;
		}
	}

	public static void createLogDirectory(String logPath) {

		if (!logPath.endsWith("\\")) {
			logPath = logPath + "\\";
		}
		String cycleReport = logPath + "cycleReport\\";
		String suiteReport = logPath + "suiteReport\\";
		String stepReport = logPath + "stepReport\\";

		File cycleReportPath = new File(cycleReport);

		if (!cycleReportPath.exists()) {
			if (cycleReportPath.mkdirs()) {
				System.out.println("Cycle Report Path directories are created!");
			} else {
				System.out.println("Failed to create Cycle Report Path directories!");
			}
		}

		File suiteReportPath = new File(suiteReport);

		if (!suiteReportPath.exists()) {
			if (suiteReportPath.mkdirs()) {
				System.out.println("Suite Report Path directories are created!");
			} else {
				System.out.println("Failed to create Suite Report Path directories!");
			}
		}

		File stepReportPath = new File(stepReport);

		if (!stepReportPath.exists()) {
			if (stepReportPath.mkdirs()) {
				System.out.println("Step Report Path directories are created!");
			} else {
				System.out.println("Failed to create Step Report Path directories!");
			}
		}

	}


	public boolean validateCondition(String condition) throws SeleniumFrameworkException{
		String[] condArray=condition.toLowerCase().split(";");
		if(condArray.length==3){
		String actualValue=condArray[0];
		String condValue=condArray[1];
		String ExpValue=condArray[2];
		switch (condValue){
		case "equals":
			if(actualValue.equalsIgnoreCase(ExpValue))
			{
				return true;
			}
			else
			{
				return false;
						
			}
			
		case "contains":
			
			if(actualValue.contains(ExpValue))
			{
				return true;
			}
			else
			{
				return false;
						
			}
		case "not":
			if(!actualValue.equalsIgnoreCase(ExpValue))
			{
				return true;
			}
			else
			{
				return false;
						
			}
		
		default:
			break;
		}
		}
		else
		{
			throw new SeleniumFrameworkException("Condition is not in correct Format");
		}
		
	
		//Split condition based on ; delimiter
		//First value is value1 or actual valie
		//second value is condition eg equals, not , contains
		// third value is exp value
		
		//Use switch cas on condition
		//For each case check if actual and exp value matches cond
		//if matches return true else false
		
		return true;
		
	}
	
	public static List<String> inputSplit(String input)
	{
		String[] inputArray=input.split("-");
		List<String> wordList = new ArrayList<String>(Arrays.asList(inputArray));
		return wordList;
	}
	
	
}
