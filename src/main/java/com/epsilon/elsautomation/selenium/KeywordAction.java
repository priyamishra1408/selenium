package com.epsilon.elsautomation.selenium;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.Assertion;
import org.testng.xml.LaunchSuite;

import com.epsilon.elsautomation.common.ReportingStatus;
import com.epsilon.elsautomation.common.Status;
import com.epsilon.elsautomation.common.Utility;
import com.epsilon.elsautomation.driver.Driver;
import com.epsilon.elsautomation.driver.DriverExecutor;
import com.epsilon.elsautomation.driver.PathHolder;
import com.epsilon.elsautomation.dto.MappingDTO;
import com.epsilon.elsautomation.dto.StepExcelDTO;
import com.epsilon.elsautomation.exception.SeleniumFrameworkException;
import com.epsilon.elsautomation.reader.ExcelReader;
import com.epsilon.elsautomation.reader.NotepadReader;
import com.gargoylesoftware.htmlunit.WebClient;

import net.sourceforge.htmlunit.corejs.javascript.ast.ForLoop;
import net.sourceforge.htmlunit.corejs.javascript.ast.WhileLoop;

public class KeywordAction {

	private static final String True = null;
	ExcelReader exclReader;

	public KeywordAction() {
		exclReader = new ExcelReader();
	}

	private int addNumValue(String str) {
		String[] strArray = str.split("\\+");

		int finalInt = 0;
		for (String s : strArray) {
			String tmp = getFinalValueFromInput(s);
			finalInt = finalInt + Integer.parseInt(tmp);
		}

		return finalInt;
	}

	public String peformGetAction(String action, WebElement webElement, StepExcelDTO stepExcelDTO,
			String subActionProperty) throws SeleniumFrameworkException {

		switch (action.toLowerCase()) {
		case "getcelldata":
			try {
				String[] propA = subActionProperty.split(",");

				int row = addNumValue(propA[0].trim());
				int column = addNumValue(propA[1].trim());

				List<WebElement> rowsTableElements = webElement.findElements(By.tagName("tr"));

				List<WebElement> columnsRowElements = rowsTableElements.get(row - 1).findElements(By.tagName("td"));

				String cellContent = columnsRowElements.get(column - 1).getText();

				System.out.println(cellContent);

				return cellContent;
			} catch (Exception e) {
				throw new SeleniumFrameworkException("getcelldata failed: " + e.getMessage());
			}

		case "getproperty":
			try {
				if (subActionProperty.equalsIgnoreCase("isObjEnabled")) {
					boolean enabled = webElement.isEnabled();
					String status = String.valueOf(enabled);
					return status;
				} else if (subActionProperty.equalsIgnoreCase("text")) {
					return webElement.getText();
				}

				else if (subActionProperty.equalsIgnoreCase("title") || subActionProperty.equalsIgnoreCase("value")
						|| subActionProperty.equalsIgnoreCase("src")) {
					return webElement.getAttribute(subActionProperty);

				} else if (subActionProperty.equalsIgnoreCase("isdisplayed")) {
					boolean visible = webElement.isDisplayed();
					String status = String.valueOf(visible);
					return status;
				}
			} catch (Exception e) {
				throw new SeleniumFrameworkException("getproperty failed: " + e.getMessage());
			}

		}
		return null;

	}

	// public void performAction(WebElement webelement, StepExcelDTO
	// stepExcelDTO) throws SeleniumFrameworkException {
	public ReportingStatus performAction(WebElement webelement, StepExcelDTO stepExcelDTO)
			throws SeleniumFrameworkException {

		ReportingStatus rs = new ReportingStatus();
		WebDriver webDriver = SeleniumWebDriver.getInstance().getWebdriver();
		String expectedcoulm = stepExcelDTO.getExpected();
		String inputColumn = stepExcelDTO.getInput();
		String actionColumn = stepExcelDTO.getAction();
		String[] actionArray = actionColumn.split(";");
		String action = actionArray[0].toLowerCase();
		String checkPropertry = null;
		if (actionArray.length > 1) {
			checkPropertry = actionArray[1];
		}

		switch (action) {

		case "click":
			try {
				webelement.click();
			} catch (Exception e) {
				throw new SeleniumFrameworkException("Failed to click element, exception is " + e.getMessage());
			}
			break;

		case "isordershipped":
			waitForOrderStatus("SHIPPED", 300);

			break;

		case "upload":
			webelement.sendKeys(getFinalValueFromInput(inputColumn));
			break;

		case "getaction":

			try {
				String[] propArr = checkPropertry.split("\\(");
				String subAction = propArr[0];
				String subActionProperty = null;
				if (propArr.length > 1) {
					subActionProperty = propArr[1];
					// subActionProperty = subActionProperty.replace(")", "");
					subActionProperty = subActionProperty.substring(0, subActionProperty.length() - 1);

				}

				String ret = peformGetAction(subAction, webelement, stepExcelDTO, subActionProperty);

				String outPut = stepExcelDTO.getOutput();
				String Expected = stepExcelDTO.getExpected();

				if (StringUtils.isNotBlank(outPut)) {
					GlobalData.variableMap.put(outPut, ret);

					if (StringUtils.isNotBlank(Expected)) {
						rs = performAssertion(stepExcelDTO);

					}
				}
			} catch (Exception e) {
				throw new SeleniumFrameworkException("Exception in getaction - " + e.getMessage());
			}

			return rs;
		// break;

		case "wait":

			try {
				String timeToWait = stepExcelDTO.getInput();
				double timeToWaitInt = Double.parseDouble(timeToWait);
				int time = (int) timeToWaitInt;
				webDriver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);

				Thread.sleep(time * 1000);
			} catch (InterruptedException e1) {
				throw new SeleniumFrameworkException(
						"Exception occured while waiting. Exception is " + e1.getMessage());
			} catch (Exception e) {
				throw new SeleniumFrameworkException("Exception in wait: " + e.getMessage());
			}
			break;

		case "waitforobject":

			try {
				WebDriverWait wait = new WebDriverWait(webDriver, 30);
				if (checkPropertry.equalsIgnoreCase("clickable")) {
					WebElement element = wait.until(ExpectedConditions.elementToBeClickable(webelement));
				} else if (checkPropertry.equalsIgnoreCase("exist")) {
					WebElement element = wait.until(ExpectedConditions.visibilityOf(webelement));
				}
			} catch (Exception e) {
				throw new SeleniumFrameworkException("Exception in waitforobject: " + e.getMessage());
			}

			break;

		case "setvariable":

			try {
				String inputText = stepExcelDTO.getInput();
				String[] inputSubString = inputText.split(",", 2);
				String variableName = inputSubString[0].trim();
				variableName = variableName.replace("\"", "");
				String evalValue = inputSubString[1].trim();
				String[] subevalValue = evalValue.split("&");
				String finalValue = "";
				for (String s : subevalValue) {
					String tmpString = "";

					if (s.toLowerCase().contains("twodemarrayvar")) {
						tmpString = evalTwoDimArrayVar(s);

					} // End of twodemarrayvar logic

					else {
						tmpString = s;

						String inputVariableValue1 = getVarName(s);

						if (inputVariableValue1 != null) {
							tmpString = GlobalData.variableMap.get(inputVariableValue1);
							if(StringUtils.isBlank(tmpString)){
								tmpString = inputVariableValue1;
							}

						}
					}
					finalValue = finalValue + tmpString;
				}
				GlobalData.variableMap.put(variableName, finalValue);
			} catch (Exception e) {
				throw new SeleniumFrameworkException("Exception in setvarible: " + e.getMessage());
			}

			break;

		case "inputtext":

			try {
				webelement.click();
				webelement.clear();

				webelement.sendKeys(getFinalValueFromInput(inputColumn));
			} catch (Exception e) {
				throw new SeleniumFrameworkException("Exception in inputtext: " + e.getMessage());
			}

			break;
		case "selectelementbynum":

			try {
				if (stepExcelDTO.getInput() != null && !stepExcelDTO.getInput().isEmpty()) {

					String component = stepExcelDTO.getComponent();

					if (StringUtils.isNotBlank(component)) {
						String tempComp = evalTwoDimArrayVar(component);
						if (StringUtils.isNotBlank(tempComp)) {
							component = tempComp;
						}
					}

					ExcelReader exlRdr = new ExcelReader();
					String mappingPath = PathHolder.getMappingFiles();

					MappingDTO mappingComponent = null;

					mappingComponent = exlRdr.searchMappingFile(mappingPath, 0, component);

					String propertyName = mappingComponent.getPropertyName();
					String propertyValue = mappingComponent.getPropertyValue();

					ElementHelper elementHelper = new ElementHelper();

					List<WebElement> elementList = elementHelper.getElementsFromProperty(propertyName, propertyValue);

					String finalvalue = getFinalValueFromInput(inputColumn);
					String[] inputNumber = finalvalue.split(",");
					for (String string : inputNumber) {

						int elementNo = Integer.parseInt(string);
						WebElement elementNoInt = elementList.get(elementNo - 1);
						// moveCordinates(webDriver,elementNoInt);
						elementNoInt.click();

					}

				} else {
					WebDriverWait wait2 = new WebDriverWait(webDriver, 10);
					WebElement element = wait2.until(ExpectedConditions.visibilityOf(webelement));

					element.click();

				}
			} catch (Exception e) {
				throw new SeleniumFrameworkException("selectelembynum failed: " + e.getMessage());
			}

			break;

		case "openbrowser":

			try {
				String[] inputArray = inputColumn.split(",");
				String browser = inputArray[0];
				String url = inputArray[1];

				SeleniumWebDriver seleniumWebDriver = SeleniumWebDriver.getInstance();

				seleniumWebDriver.launchWebDriver(browser, url);
				//
			} catch (Exception e) {
				throw new SeleniumFrameworkException("openbrowser failed: " + e.getMessage());
			}
			break;

		case "closebrowser":

			try {
				webDriver.close();
			} catch (Exception e) {
				throw new SeleniumFrameworkException("closebrowser failed: " + e.getMessage());
			}
			break;

		case "waitforcount":
			waitforcount(stepExcelDTO);
			break;

		case "istherezerocount":
			isThereZeroCount(actionColumn, stepExcelDTO);
			break;

		case "clickifexits":
			try {

				String component = stepExcelDTO.getComponent();

				if (StringUtils.isNotBlank(component)) {
					String tempComp = evalTwoDimArrayVar(component);
					if (StringUtils.isNotBlank(tempComp)) {
						component = tempComp;
					}
				}

				ExcelReader exlRdr = new ExcelReader();
				String mappingPath = PathHolder.getMappingFiles();

				MappingDTO mappingComponent = null;

				mappingComponent = exlRdr.searchMappingFile(mappingPath, 0, component);

				String propertyName = mappingComponent.getPropertyName();
				String propertyValue = mappingComponent.getPropertyValue();

				ElementHelper elementHelper = new ElementHelper();
				List<WebElement> elementList = elementHelper.getElementsFromProperty(propertyName, propertyValue);

				if (elementList.size() != 0) {
					elementList.get(0).click();
				}
			} catch (Exception e) {

				throw new SeleniumFrameworkException("Eexception" + e.getMessage());
			}
			break;

		// case "clickifexits":
		// try {
		//
		// String component = stepExcelDTO.getComponent();
		//
		// ExcelReader exlRdr = new ExcelReader();
		// String mappingPath = PathHolder.getMappingFiles();
		//
		// MappingDTO mappingComponent = null;
		//
		// mappingComponent = exlRdr.searchMappingFile(mappingPath, 0,
		// component);
		//
		// String propertyName = mappingComponent.getPropertyName();
		// String propertyValue = mappingComponent.getPropertyValue();
		//
		// ElementHelper elementHelper = new ElementHelper();
		//
		// List<WebElement> elementList =
		// elementHelper.getElementsFromProperty(propertyName, propertyValue);
		// for (WebElement elem : elementList) {
		// if(elem.isEnabled()){
		// webelement.click();
		// }
		//
		//
		// }
		// }catch (Exception e) {
		// throw new SeleniumFrameworkException("Failed to click element,
		// exception is " + e.getMessage());
		// }
		// break;

		case "selectcell":

			try {
				String performAction = null;
				if (actionArray.length == 3) {
					performAction = actionArray[2];
				}

				String[] propArr1 = checkPropertry.split("\\(", 2);
				String subAction1 = propArr1[0];
				String subActionProperty1;
				if (propArr1.length > 1) {
					subActionProperty1 = propArr1[1];
					subActionProperty1 = subActionProperty1.replace(")", "");

					String[] propA1 = subActionProperty1.split(",");
					// int row = Integer.parseInt(propA1[0]);
					int row = addNumValue(propA1[0].trim());

					// int column = Integer.parseInt(propA1[1]);
					int column = addNumValue(propA1[1].trim());

					List<WebElement> rowsTable1 = webelement.findElements(By.tagName("tr"));

					List<WebElement> columnsRow1 = rowsTable1.get(row - 1).findElements(By.tagName("td"));

					WebElement cellElement = columnsRow1.get(column - 1);

					String inputVal = stepExcelDTO.getInput();
					String inputSplit[] = inputVal.split(";");

					// TODO Split using;
					// first value is xpath
					// secodn value with getFinalValueFromInput is settext value

					if (StringUtils.isNotBlank(inputVal)) {
						List<WebElement> cellContent = cellElement.findElements(By.xpath(inputSplit[0]));
						if (cellContent.size() == 1) {
							cellElement = cellContent.get(0);
						}

					}

					switch (performAction.toLowerCase()) {

					case "click":

						cellElement.click();

						break;

					case "settext":
						cellElement.click();
						cellElement.clear();
						cellElement.sendKeys(getFinalValueFromInput(inputSplit[1]));
						break;

					case "select":
						Select select = new Select(cellElement);
						select.selectByValue(getFinalValueFromInput(inputSplit[1]));

						break;

					default:
						break;
					}
				}
			} catch (Exception e) {
				throw new SeleniumFrameworkException("selectcell failed: " + e.getMessage());
			}

			break;

		case "selectelement":

			try {
				if (stepExcelDTO.getInput() != null && !stepExcelDTO.getInput().isEmpty()) {

					String component = stepExcelDTO.getComponent();

					if (StringUtils.isNotBlank(component)) {
						String tempComp = evalTwoDimArrayVar(component);
						if (StringUtils.isNotBlank(tempComp)) {
							component = tempComp;
						}
					}

					ExcelReader exlRdr = new ExcelReader();
					String mappingPath = PathHolder.getMappingFiles();

					MappingDTO mappingComponent = null;

					mappingComponent = exlRdr.searchMappingFile(mappingPath, 0, component);

					String propertyName = mappingComponent.getPropertyName();
					String propertyValue = mappingComponent.getPropertyValue();

					ElementHelper elementHelper = new ElementHelper();

					List<WebElement> elementList = elementHelper.getElementsFromProperty(propertyName, propertyValue);

					String finalvalue = getFinalValueFromInput(inputColumn);

					// TODO Write a method in Utility class to split finalValue
					// using - delimiter and retun List<String>
					// Store List<String> in varaible strList

					List<String> strList = Utility.inputSplit(finalvalue);

					for (WebElement elem : elementList) {

						if (strList.contains(elem.getAttribute("value"))) {

							WebDriverWait wait1 = new WebDriverWait(webDriver, 10);
							WebElement finalElem = wait1.until(ExpectedConditions.visibilityOf(elem));

							// moveCordinates(webDriver, finalElem);
							finalElem.click();

							if (strList.size() == 1) {
								return rs;
							}
						}

					}
				} else {
					WebDriverWait wait2 = new WebDriverWait(webDriver, 10);
					WebElement final1 = wait2.until(ExpectedConditions.visibilityOf(webelement));

					final1.click();

				}
			} catch (Exception e) {
				throw new SeleniumFrameworkException("selectelem failed: " + e.getMessage());
			}

			break;

		case "fireevent":

			try {
				Actions action1 = new Actions(webDriver);
				action1.moveToElement(webelement).build().perform();
			} catch (Exception e) {
				throw new SeleniumFrameworkException("fireevent failed: " + e.getMessage());
			}
			break;

		case "moveclick":
			try {
				moveCordinates(webDriver, webelement);
				webelement.click();
			} catch (Exception e) {
				throw new SeleniumFrameworkException("moveclick failed: " + e.getMessage());
			}
			break;

		case "selectlistitem":

			try {
				if (stepExcelDTO.getInput() != null && !stepExcelDTO.getInput().isEmpty()) {

					String component = stepExcelDTO.getComponent();

					if (StringUtils.isNotBlank(component)) {
						String tempComp = evalTwoDimArrayVar(component);
						if (StringUtils.isNotBlank(tempComp)) {
							component = tempComp;
						}
					}

					String input = stepExcelDTO.getInput();
					int inoutint = Integer.parseInt(input);

					ExcelReader exlRdr = new ExcelReader();
					String mappingPath = PathHolder.getMappingFiles();

					MappingDTO mappingComponent = null;
					mappingComponent = exlRdr.searchMappingFile(mappingPath, 0, component);

					String propertyName = mappingComponent.getPropertyName();
					String propertyValue = mappingComponent.getPropertyValue();

					ElementHelper elementHelper = new ElementHelper();

					List<WebElement> elementList = elementHelper.getElementsFromProperty(propertyName, propertyValue);

					for (WebElement elem : elementList) {

						String finalvalue = getFinalValueFromInput(input);

						if (elem.getAttribute("value").equals(finalvalue)) {
							WebElement element = elementList.get(inoutint);
							Select select = new Select(element);
							return rs;
						}

						else {

							Select select = new Select(elem);

						}

					}
				}
			} catch (Exception e) {
				throw new SeleniumFrameworkException("selectlist failed: " + e.getMessage());
			}

			break;

		case "select":

			String selectBy = actionArray[1];
			Select select = new Select(webelement);
			if (selectBy.equalsIgnoreCase("value")) {

				select.selectByValue(getFinalValueFromInput(inputColumn));
			} else if (selectBy.equalsIgnoreCase("index")) {
				select.selectByIndex(Integer.parseInt(getFinalValueFromInput(inputColumn)));

			} else if (selectBy.equalsIgnoreCase("visibletext")) {
				select.selectByVisibleText(getFinalValueFromInput(inputColumn));

			}

			break;

		}
		return rs;
	}

	private String getVarName(String str) {

		if (str.trim().toUpperCase().startsWith("VAR")) {

			String inputArr[] = str.split("\\(");
			inputArr[1] = inputArr[1].replace("\"", "");
			inputArr[1] = inputArr[1].replace(")", "");

			String inputinnerText = inputArr[1];

			return inputinnerText.trim();

		}
		return str.trim();

	}

	public String evalTwoDimArrayVar(String s) {
		String tmpString = null;

		if (s.toLowerCase().contains("twodemarrayvar")) {
			String[] subArray = s.split(",");
			String str1 = subArray[0];
			String str2 = subArray[1];
			String str3 = subArray[2];
			String[] subStr1 = str1.split("\\(");
			String varValue1 = subStr1[1];
			varValue1 = varValue1.replace("\"", "");
			String varValue2 = str2;
			String varValue3 = str3.replace(")", "");

			String inputVariableValue2 = getVarName(varValue2);
			String inputVariableValue3 = getVarName(varValue3);

			if (inputVariableValue2 != null) {
				varValue2 = GlobalData.variableMap.get(inputVariableValue2);

			}
			if (inputVariableValue3 != null) {
				varValue3 = GlobalData.variableMap.get(inputVariableValue3);
				if (varValue3 == null) {
					varValue3 = inputVariableValue3;

				}
			}

			int rowno = Integer.parseInt(varValue2);
			int columnno = Integer.parseInt(varValue3);
			tmpString = GlobalData.dataMap.get(varValue1)[rowno][columnno];

		}
		return tmpString;

	}

	public String getFinalValueFromInput(String inputText) {

		String[] subevalValue = inputText.split("&");
		String finalValue = "";
		for (String s : subevalValue) {
			String tmpString = "";

			if (s.toLowerCase().contains("twodemarrayvar")) {
				tmpString = evalTwoDimArrayVar(s);

			} // End of twodemarrayvar logic
			else if (s.toLowerCase().contains("+")) {
				int tmpInt = addNumValue(s);
				tmpString = String.valueOf(tmpInt);
			} else {
				tmpString = s;

				String inputVariableValue1 = getVarName(s);

				if (inputVariableValue1 != null) {
					tmpString = GlobalData.variableMap.get(inputVariableValue1);
					if (tmpString == null) {
						tmpString = inputVariableValue1;
						tmpString = tmpString.replace("\"", "");
					}
				}

			}

			finalValue = finalValue + tmpString;
		}
		return finalValue;
	}

	public ReportingStatus performAssertion(StepExcelDTO stepExcelDTO) {

		ReportingStatus rs = new ReportingStatus();
		String tmpExpColumn = stepExcelDTO.getExpected();
		String output = stepExcelDTO.getOutput();

		String finalOutput = GlobalData.variableMap.get(output);
		String[] arryColmn = tmpExpColumn.split(";");
		String expectedColumn = arryColmn[0];
		String assertType = null;
		String msg = stepExcelDTO.getNotes();
		if (StringUtils.isBlank(msg)) {
			msg = "Validate actual and expected value";

		}
		if (arryColmn.length > 1) {
			assertType = arryColmn[1];
		}

		// TODO Read notes column and store it in variable msg
		// Check if msg is null then make msg as "Actual and Expected Value is
		// not matching"
		// When assertion fails then it will display this msg
		String finalExpected = "";

		if (expectedColumn.toLowerCase().contains("twodemarrayvar")) {
			finalExpected = evalTwoDimArrayVar(expectedColumn);

		} // End of twodemarrayvar logic

		else {

			String tmpExpected = getVarName(expectedColumn);

			finalExpected = GlobalData.variableMap.get(tmpExpected);

			if (StringUtils.isBlank(finalExpected)) {
				finalExpected = tmpExpected;
			}
		}

		try {

			if (StringUtils.isBlank(assertType)) {
				if (finalOutput.equalsIgnoreCase(finalExpected)) {
					rs.setCurStatus(Status.PASS);
					rs.setDescription(msg + " - PASS");
				} else {
					rs.setCurStatus(Status.FAIL);
					rs.setDescription(msg + " - FAIL");
				}
				// Assert.assertEquals(finalOutput, finalExpected, msg);
			} else if (assertType.toLowerCase().equalsIgnoreCase("contains")) {
				if (finalOutput.contains(finalExpected)) {
					rs.setCurStatus(Status.PASS);
					rs.setDescription(msg + " - PASS");
				} else {
					rs.setCurStatus(Status.FAIL);
					rs.setDescription(msg + " - FAIL");
				}
				// Assert.assertEquals(finalOutput.contains(finalExpected),
				// true, msg);
			} else if (assertType.toLowerCase().equalsIgnoreCase("not")) {
				if (!finalOutput.equalsIgnoreCase(finalExpected)) {
					rs.setCurStatus(Status.PASS);
					rs.setDescription(msg + " - PASS");
				} else {
					rs.setCurStatus(Status.FAIL);
					rs.setDescription(msg + " - FAIL");
				}
				// Assert.assertNotEquals(finalOutput, finalExpected, msg);
			}

			return rs;

		} catch (Exception ex) {
			rs.setCurStatus(Status.FAIL);
			rs.setDescription(msg + " - FAIL");
			return rs;
			// System.out.print("exception");
		}
	}

	public void countAndWriteLastSeg() {

		// String reportPath=GlobalData.globalSheetMap.

	}

	private void moveCordinates(WebDriver driver, WebElement webElement) throws SeleniumFrameworkException {

		try {
			Robot robot = new Robot();
			robot.setAutoDelay(2000);
			robot.mouseMove(webElement.getLocation().getX() + 100, webElement.getLocation().getY());
			robot.setAutoDelay(2000);

			// Scroll the browser to the element's Y position
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + (webElement.getLocation().y + 20) + ")");

		} catch (AWTException e) {
			throw new SeleniumFrameworkException("movecordinat failed: " + e.getMessage());
		}
	}

	// TODO Read notes column and store it in variable msg
	// Check if msg is null then make msg as "Actual and Expected Value is
	// not matching"
	// When assertion fails then it will display this msg

	// TODO Use if else on assertType
	// if asserttype is null then Assert.assertEquals(finalOutput,
	// finalExpected,msg);
	// else if asserttype is contains then
	// Assert.assertEquals(finalOutput.contains(finalExpected), true,msg);

	// TODO wrap assert lines in try catch statement
	// Catch AssertionError
	// If we will not catch then execution will stop if a test fails
	// TODO fix test case and use correct assertion at correct places

	public void getCountForObject(String action) throws SeleniumFrameworkException {

		StepExcelDTO step = new StepExcelDTO();

		if (action.equalsIgnoreCase("waitforcount")) {

			if (step.getInput() != null && !step.getInput().isEmpty()) {

				String component = step.getComponent();
				if (StringUtils.isNotBlank(component)) {
					String tempComp = evalTwoDimArrayVar(component);
					if (StringUtils.isNotBlank(tempComp)) {
						component = tempComp;
					}
				}

				String input = step.getInput();
				int inputint = Integer.parseInt(input);
				ExcelReader exlRdr = new ExcelReader();
				String mappingPath = PathHolder.getMappingFiles();
				MappingDTO mappingComponent = null;
				mappingComponent = exlRdr.searchMappingFile(mappingPath, 0, component);

				String propertyName = mappingComponent.getPropertyName();
				String propertyValue = mappingComponent.getPropertyValue();

				ElementHelper elementHelper = new ElementHelper();

				List<WebElement> nCount = elementHelper.getElementsFromProperty(propertyName, propertyValue);

				for (WebElement elem : nCount) {

					String countText = elem.getText();

					if (countText.equalsIgnoreCase("CountOffline")) {

						WebElement home = elementHelper.getElementFromProperty(propertyName, propertyValue);
					}

				}

			}
		}

	}

	public void waitforcount(StepExcelDTO step) throws SeleniumFrameworkException {
		int MyCount;

		String component = step.getComponent();
		if (StringUtils.isNotBlank(component)) {
			String tempComp = evalTwoDimArrayVar(component);
			if (StringUtils.isNotBlank(tempComp)) {
				component = tempComp;
			}
		}
		String inputvalue = step.getInput();
		int inputint = Integer.parseInt(inputvalue);
		MyCount = inputint;
		ExcelReader exlRdr = new ExcelReader();
		String mappingPath = PathHolder.getMappingFiles();
		// MappingDTO mappingComponent = exlRdr.searchMappingFile(mappingPath,
		// 0, component);
		//
		// String propertyName = mappingComponent.getPropertyName();
		// String propertyValue = mappingComponent.getPropertyValue();

		ElementHelper elementHelper = new ElementHelper();

		MappingDTO mapping1 = exlRdr.searchMappingFile(PathHolder.getMappingFiles(), 0, component);
		List<WebElement> SegCountDetailTbl = elementHelper.getElementsFromProperty(mapping1.getPropertyName(),
				mapping1.getPropertyValue());
		int sengtabelcount = SegCountDetailTbl.size();

		MappingDTO mapping2 = exlRdr.searchMappingFile(PathHolder.getMappingFiles(), 0, "CountOffline");
		List<WebElement> CountOffline = elementHelper.getElementsFromProperty(mapping2.getPropertyName(),
				mapping2.getPropertyValue());
		int countoffcount = CountOffline.size();

		MappingDTO mapping3 = exlRdr.searchMappingFile(PathHolder.getMappingFiles(), 0, "Countfailed");
		List<WebElement> Countfailed = elementHelper.getElementsFromProperty(mapping3.getPropertyName(),
				mapping3.getPropertyValue());
		int failedcount = Countfailed.size();

		int RunCountFailed = 0;
		while (MyCount > 0) {

			if (sengtabelcount > 0) {
				return;
			} else if (countoffcount > 0) {

				waitForOrderStatus("Count Ready", inputint);
				List<StepExcelDTO> openOrder = exlRdr.readStepExcel(mappingPath + "OpenOrder.xls", 0);
				DriverExecutor driver = new DriverExecutor();
				driver.runStepTest(openOrder, null);
				return;
			} else if (failedcount > 0) {

				MappingDTO mapping4 = exlRdr.searchMappingFile(PathHolder.getMappingFiles(), 0, "runcount");
				WebElement runCount = elementHelper.getElementFromProperty(mapping4.getPropertyName(),
						mapping4.getPropertyValue());
				if (RunCountFailed <= 2 && runCount.isDisplayed()) {
					runCount.click();
					RunCountFailed = RunCountFailed + 1;
				} else {
					GlobalData.globalSheetMap.put("skipSuite", "true");
					throw new SeleniumFrameworkException("Run Count Failed after another attempt.");
				}
			}

			else {

				try {
					Thread.sleep(5 * 1000);
					MyCount = MyCount - 5;
				} catch (InterruptedException e) {
					throw new SeleniumFrameworkException(e.getMessage());
				}

			}

		}

		throw new SeleniumFrameworkException(
				"Count Offline is not ready after " + inputint + " seconds for order " + "<Order Number>");

	}

	public void waitForOrderStatus(String Status, int count) throws SeleniumFrameworkException {
		int MyCount;

		// String component = step.getComponent();
		// String inputvalue = step.getInput();
		int inputint = count;
		MyCount = inputint;
		ExcelReader exlRdr = new ExcelReader();
		String mappingPath = PathHolder.getTestFiles();
		ElementHelper elementHelper = new ElementHelper();

		MappingDTO mapping1 = exlRdr.searchMappingFile(PathHolder.getMappingFiles(), 0, "SearchId");
		List<WebElement> searchID = elementHelper.getElementsFromProperty(mapping1.getPropertyName(),
				mapping1.getPropertyValue());
		MappingDTO mapping2 = exlRdr.searchMappingFile(PathHolder.getMappingFiles(), 0, "Home");
		WebElement Home = elementHelper.getElementFromProperty(mapping2.getPropertyName(), mapping2.getPropertyValue());

		if (searchID.size() < 1) {
			try {
				Home.click();

			} catch (Exception Ex) {
				List<StepExcelDTO> steppath = exlRdr.readStepExcel(mappingPath + "StepLaunchETLSSite.xls", 0);
				DriverExecutor driver = new DriverExecutor();
				driver.runStepTest(steppath, null);

			}
		}

		while (MyCount > 0) {
			List<StepExcelDTO> steppath1 = exlRdr.readStepExcel(mappingPath + "StepFindOrder.xls", 0);
			DriverExecutor driver = new DriverExecutor();
			driver.runStepTest(steppath1, null);

			String findOrderstatus = GlobalData.variableMap.get("Status");

			if (findOrderstatus.equalsIgnoreCase(Status)) {
				Home = elementHelper.getElementFromProperty(mapping2.getPropertyName(), mapping2.getPropertyValue());
				Home.click();
				return;

			} else {
				try {
					Thread.sleep(5 * 1000);
					Home = elementHelper.getElementFromProperty(mapping2.getPropertyName(),
							mapping2.getPropertyValue());
					Home.click();
					MyCount = MyCount - 5;
				} catch (InterruptedException e) {
					throw new SeleniumFrameworkException(e.getMessage());
				}

			}

		}

	}

	public void isThereZeroCount(String action, StepExcelDTO step) throws SeleniumFrameworkException {

		{

			String component = step.getComponent();

			if (StringUtils.isNotBlank(component)) {
				String tempComp = evalTwoDimArrayVar(component);
				if (StringUtils.isNotBlank(tempComp)) {
					component = tempComp;
				}
			}

			ExcelReader exlRdr = new ExcelReader();
			String mappingPath = PathHolder.getMappingFiles();
			MappingDTO mappingComponent = null;
			mappingComponent = exlRdr.searchMappingFile(mappingPath, 0, component);

			String propertyName = mappingComponent.getPropertyName();
			String propertyValue = mappingComponent.getPropertyValue();

			ElementHelper elementHelper = new ElementHelper();

			WebElement Segmenttable = elementHelper.getElementFromProperty(propertyName, propertyValue);
			List<WebElement> rowsTableElements = Segmenttable.findElements(By.tagName("tr"));

			int row = rowsTableElements.size();
			int column = 9;

			for (int i = 1; i < row; i++) {
				List<WebElement> columnsRow1 = rowsTableElements.get(i).findElements(By.tagName("td"));

				WebElement cellElement = columnsRow1.get(column - 1);

				String count = cellElement.getText();
				String finalCnt = count.replace(",", "");
				int countint = Integer.parseInt(finalCnt);
				if (countint == 0) {
					GlobalData.globalSheetMap.put("skipSuite", "true");
					throw new SeleniumFrameworkException("Count is zero for segment in row " + (i + 1));

				}
			}

		}

	}

}