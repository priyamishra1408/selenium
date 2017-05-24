package com.epsilon.elsautomation.driver;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import com.epsilon.elsautomation.common.ReportingStatus;
import com.epsilon.elsautomation.common.Status;
import com.epsilon.elsautomation.common.Utility;
import com.epsilon.elsautomation.dto.CycleSuiteExcelDTO;
import com.epsilon.elsautomation.dto.MappingDTO;
import com.epsilon.elsautomation.dto.ReportingDTO;
import com.epsilon.elsautomation.dto.StepExcelDTO;
import com.epsilon.elsautomation.exception.SeleniumFrameworkException;
import com.epsilon.elsautomation.reader.ExcelReader;
import com.epsilon.elsautomation.reader.NotepadReader;
import com.epsilon.elsautomation.report.ELSReportingHelper;
import com.epsilon.elsautomation.report.ReporterHelper;
import com.epsilon.elsautomation.selenium.ElementHelper;
import com.epsilon.elsautomation.selenium.GlobalData;
import com.epsilon.elsautomation.selenium.KeywordAction;

public class DriverExecutor {

	private final NotepadReader notepadReader;
	private final ExcelReader excelReader;
	private final KeywordAction keywordAction;
	private final ElementHelper elementHelper;
	private static String startTime;
	private boolean stepResult;
	private static DateFormat dateFormat;
	private static String startELSReportPath;

	List<String> independenActions = new ArrayList<>();

	public static String getStartTime() {
		return startTime;
	}

	public DriverExecutor() {
		this.notepadReader = new NotepadReader();
		this.excelReader = new ExcelReader();
		this.keywordAction = new KeywordAction();
		this.elementHelper = new ElementHelper();
		independenActions.add("openbrowser");
		independenActions.add("closebrowser");
		independenActions.add("setvariable");
		independenActions.add("wait");
		independenActions.add("condition");
		independenActions.add("clickifexits");
		independenActions.add("isordershipped");

		Date dt = new Date();
		dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss.SSS");
		startTime = dateFormat.format(dt);
	}

	public void runTest(String startFile) {

		// Full location of start File
		String startFilePath = PathHolder.getMainFolder() + startFile;

		// Read startFile
		List<String> cycleRegressionList = notepadReader.readNotePad(startFilePath);

		/**
		 * TODO Load Global Data File by calling excelReader.readGlobalSheet
		 * Path of global file is PathHolder.globalDataFile and sheet number is
		 * 0
		 */

		String startReportPath = PathHolder.getReportPath() + startFile + startTime + ".html";
		startELSReportPath = PathHolder.getReportPath() + "ELSAutomationReport" + startTime + ".html";

		// Start creating suite report file
		ELSReportingHelper.initReport(startELSReportPath, GlobalData.globalSheetMap.get("username"));
		ReporterHelper.createStartTestHtml(startReportPath, startFile);

		// Iterate each line of start test file
		for (String cycleRegressionVar : cycleRegressionList) {
			try {
				// From given string get excel name and sheet number, first
				// element
				// is name and 2nd is sheet
				List<String> list = Utility.getExcelNameSheet(cycleRegressionVar);

				// Get full path of cycle regression
				String cycleRegressionPath = PathHolder.getTestFiles() + list.get(0) + ".xls";

				// Run this cycleRegression File

				List<CycleSuiteExcelDTO> cycleDto = excelReader.readCycleSuiteExcel(cycleRegressionPath,
						Integer.parseInt(list.get(1)));

				String cycleReportPath = PathHolder.getCycleReportpath() + cycleRegressionVar + startTime + ".html";

				// Start creating suite report file
				ReporterHelper.createTestSuiteHtml(cycleReportPath, cycleRegressionVar);
				ELSReportingHelper.addCycleRegression(startELSReportPath, cycleRegressionVar);

				// For each line of cycle excel file
				boolean cycleResult = runCycleTest(cycleDto, cycleReportPath);

				ReportingStatus rs = new ReportingStatus();
				if (cycleResult) {
					rs.setCurStatus(Status.PASS);
				} else {
					rs.setCurStatus(Status.FAIL);
				}
				ReporterHelper.updateStartTest(startReportPath, cycleRegressionVar, rs, startTime);

			} catch (SeleniumFrameworkException e) {
				ReportingStatus rs = new ReportingStatus();
				rs.setCurStatus(Status.FAIL);
				rs.setDescription(e.getMessage());
				ReporterHelper.updateStartTest(startReportPath, cycleRegressionVar, rs, startTime);

			}

		}

	}

	public boolean runCycleTest(List<CycleSuiteExcelDTO> cycleSuiteExcelDTOs, String cycleReportPath) {

		boolean returnValue = true;

		for (CycleSuiteExcelDTO cycleSuiteExcelDTO : cycleSuiteExcelDTOs) {

			try {

				// Only if Execute column is Y we need to run
				if (StringUtils.isNotBlank(cycleSuiteExcelDTO.getExecute())
						&& cycleSuiteExcelDTO.getExecute().equalsIgnoreCase("Y")) {
					String reference = cycleSuiteExcelDTO.getTableReference();
					int suiteSheetNum = cycleSuiteExcelDTO.getSheetToRun();

					// If it not suite file we need to run step file
					// It it is suite file we need to run each line of suite
					// file
					List<CycleSuiteExcelDTO> tmpDto = new ArrayList<>();
					tmpDto.add(cycleSuiteExcelDTO);

					String reportPath = null;
					if (reference.toLowerCase().startsWith("suite")) {
						// Read Suite File
						String suiteRegressionPath = PathHolder.getTestFiles() + reference + ".xls";
						tmpDto = excelReader.readCycleSuiteExcel(suiteRegressionPath, suiteSheetNum);

						reportPath = PathHolder.getSuiteReportpath() + reference + "(" + suiteSheetNum + ")" + startTime
								+ ".html";

						// Start creating suite report file
						ReporterHelper.createTestSuiteHtml(reportPath, reference + "(" + suiteSheetNum + ")");
					}

					ReportingDTO reportingDTO = new ReportingDTO();
					Date dt = new Date();

					reportingDTO.setStartTime(dateFormat.format(dt));

					boolean suiteResult = runSuiteTest(tmpDto, reportPath);

					returnValue = suiteResult;

					ReportingStatus rs = new ReportingStatus();
					if (suiteResult) {
						rs.setCurStatus(Status.PASS);
					} else {
						rs.setCurStatus(Status.FAIL);
					}

					ReporterHelper.updateSuiteReport(cycleReportPath, cycleSuiteExcelDTO, rs, startTime);

					ReportingStatus rs1 = new ReportingStatus();
					if (reference.toLowerCase().startsWith("suite")) {

						if (GlobalData.variableMap.get("Status").equalsIgnoreCase("SHIPPED")) {
							rs1.setCurStatus(Status.PASS);
						} else {
							rs1.setCurStatus(Status.FAIL);
						}

						reportingDTO.setCount(GlobalData.variableMap.get("sOrderCount"));
						reportingDTO.setOrderName(GlobalData.variableMap.get("sOrderName"));
						reportingDTO.setOrderNum(GlobalData.variableMap.get("sOrderNumber"));
						reportingDTO.setTestName(reference + "(" + (suiteSheetNum + 1) + ")");

						ELSReportingHelper.updateSuiteReport(startELSReportPath, reportingDTO, rs1);
					}

				}
			} catch (SeleniumFrameworkException e) {
				returnValue = false;
				ReportingStatus rs = new ReportingStatus();
				rs.setCurStatus(Status.FAIL);
				rs.setDescription(e.getMessage());
				ReporterHelper.updateSuiteReport(cycleReportPath, cycleSuiteExcelDTO, rs, startTime);

			}
		}

		return returnValue;
	}

	/**
	 * Method to run suite file
	 */
	public boolean runSuiteTest(List<CycleSuiteExcelDTO> cycleSuiteExcelDTOs, String suiteReportPath) {
		// For all line of suite file OR current line of Cycle
		// file
		// GlobalData.globalSheetMap.put("skipSuite", "false");
		boolean retValue = true;
		for (CycleSuiteExcelDTO dto : cycleSuiteExcelDTOs) {
			try {
				// Only if Execute column is Y we need to run
				if (StringUtils.isNotBlank(dto.getExecute()) && dto.getExecute().equalsIgnoreCase("Y")) {
					boolean stepTestResult = true;
					boolean isFail = false;
					String stepFile = dto.getTableReference();
					int stepSheet = dto.getSheetToRun();
					String variableFile = dto.getVariableFile();
					String variableName = dto.getVariableName();

					// Get full path for step excel file
					String stepFilePath = PathHolder.getTestFiles() + stepFile + ".xls";

					// Read and load step excel file
					List<StepExcelDTO> stepDtoList = excelReader.readStepExcel(stepFilePath, stepSheet);
					int length = 1;

					if (StringUtils.isNotBlank(variableFile)) {

						// Get Variable file name and sheet number
						List<String> tmpList = Utility.getExcelNameSheet(variableFile);

						// Get pull path for data file
						String dataFilePath = PathHolder.getDataFiles() + tmpList.get(0) + ".xls";

						// load data file in memory
						String[][] variableData = excelReader.readDataList(Integer.parseInt(tmpList.get(1)),
								dataFilePath, variableName);
						length = variableData.length;

					}

					String reportPath = PathHolder.getStepReportpath() + dto.getTableReference() + "("
							+ dto.getSheetToRun() + ")" + startTime + ".html";

					// Start creating suite report file
					ReporterHelper.createTestStepHtml(reportPath,
							dto.getTableReference() + "(" + dto.getSheetToRun() + ")");

					ReportingStatus rs = new ReportingStatus();

					// Run this step excel for all rows
					for (int i = 0; i < length; i++) {
						// Store iteration as nIteration variable in
						// map, this is used to get variable
						GlobalData.variableMap.put("nIteration", String.valueOf(i));
						// Print Itr Num in Report
						ReporterHelper.writeInReport(reportPath, "Iteration Number : " + (i + 1));

						// Run each line of step excel
						stepTestResult = runStepTest(stepDtoList, reportPath);
						if (!stepTestResult) {
							isFail = true;
							retValue = false;
						}

					}

					if (!isFail) {
						rs.setCurStatus(Status.PASS);
					} else {
						rs.setCurStatus(Status.FAIL);
					}
					if (StringUtils.isNotBlank(suiteReportPath))
						ReporterHelper.updateSuiteReport(suiteReportPath, dto, rs, startTime);

				}
			} catch (SeleniumFrameworkException e) {
				retValue = false;
				ReportingStatus rs = new ReportingStatus();
				rs.setCurStatus(Status.FAIL);
				rs.setDescription(e.getMessage());

				if (StringUtils.isNotBlank(suiteReportPath)) {
					ReporterHelper.updateSuiteReport(suiteReportPath, dto, rs, startTime);
				}
			}
		}

		return retValue;
	}

	/**
	 * Method to run step File
	 * 
	 * @param stepExcelDTOList
	 * @throws SeleniumFrameworkException
	 */
	public boolean runStepTest(final List<StepExcelDTO> stepExcelDTOList, String reportPath)
			throws SeleniumFrameworkException {

		boolean isPass = true;

		for (StepExcelDTO stepExcelDTO : stepExcelDTOList) {

			// Step Execution Time
			Date curStepTime = new Date();

			// String skipSuite = GlobalData.globalSheetMap.get("skipSuite");
			//
			// MappingDTO mapping2 =
			// excelReader.searchMappingFile(PathHolder.getMappingFiles(), 0,
			// "Home");
			// WebElement Home =
			// elementHelper.getElementFromProperty(mapping2.getPropertyName(),
			// mapping2.getPropertyValue());
			//
			// if(StringUtils.isNotBlank(skipSuite) &&
			// skipSuite.equalsIgnoreCase("true")){
			// Home.click();
			//
			// return false;
			// }

			if (StringUtils.isNotBlank(stepExcelDTO.getExecute()) && stepExcelDTO.getExecute().equalsIgnoreCase("Y")) {

				String component = stepExcelDTO.getComponent();

				if (StringUtils.isNotBlank(component)) {
					String tempComp = keywordAction.evalTwoDimArrayVar(component);
					if (StringUtils.isNotBlank(tempComp)) {
						component = tempComp;
					}
				}

				// stepExcelDTO.setComponent(component);

				// Search component in mapping file
				// to get property name and value
				MappingDTO mappingDTO;
				try {
					mappingDTO = excelReader.searchMappingFile(PathHolder.getMappingFiles(), 0, component);

					WebElement webelement = null;
					if (mappingDTO != null && !stepExcelDTO.getAction().equalsIgnoreCase("clickifexits")) {
						// Find Selenium webelement
						// using property name and
						// value

						webelement = elementHelper.getElementFromProperty(mappingDTO.getPropertyName(),
								mappingDTO.getPropertyValue());
					}

					if (webelement != null || independenActions.contains(stepExcelDTO.getAction().toLowerCase())) {

						ReportingStatus rs = keywordAction.performAction(webelement, stepExcelDTO);
						// ReportingStatus rs = new ReportingStatus();
						rs.setCurStatus(Status.PASS);
						if (StringUtils.isNotBlank(reportPath)) {
							ReporterHelper.updateStepReport(reportPath, stepExcelDTO, rs);
						}

					} else {

						ReportingStatus rs = new ReportingStatus();
						rs.setCurStatus(Status.FAIL);

						if (webelement == null) {
							rs.setDescription("Mapping not found for componenent <" + stepExcelDTO.getComponent()
									+ "> with property " + mappingDTO);
						} else {
							rs.setDescription("Component is required for this action. Component found "
									+ stepExcelDTO.getComponent() + " for Action " + stepExcelDTO.getAction());
						}

						if (StringUtils.isNotBlank(reportPath)) {
							ReporterHelper.updateStepReport(reportPath, stepExcelDTO, rs);
						}
						System.out.println("Mapping not found for " + stepExcelDTO);
						isPass = false;
					}

				} catch (SeleniumFrameworkException e) {
					ReportingStatus rs = new ReportingStatus();
					rs.setCurStatus(Status.FAIL);
					rs.setDescription(e.getMessage());
					if (StringUtils.isNotBlank(reportPath)) {
						ReporterHelper.updateStepReport(reportPath, stepExcelDTO, rs);
					}
					isPass = false;
				}
			}
		}

		return isPass;

	}

}
