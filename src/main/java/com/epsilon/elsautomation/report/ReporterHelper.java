package com.epsilon.elsautomation.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.epsilon.elsautomation.common.ReportingStatus;
import com.epsilon.elsautomation.common.Status;
import com.epsilon.elsautomation.dto.CycleSuiteExcelDTO;
import com.epsilon.elsautomation.dto.StepExcelDTO;

public class ReporterHelper {

	static BufferedWriter bw = null;

	public static void createStartTestHtml(String reportPath, String testCase) {

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath))) {
			bw.write("<HTML><HEAD><SCRIPT>");
			bw.write("function resizeIframe(obj) {" + "console.log(\"resizeIframe\");"
					+ "obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';" + "}" + "\n"
					+ "function toogleIframe(obj) {" + "console.log(\"Show hide\");"
					+ "var visibility = document.getElementById(obj).style.visibility;" + "console.log(visibility);"
					+ "if (visibility == \"hidden\")" + "document.getElementById(obj).style.visibility='visible';"
					+ "else" + "\n" + "document.getElementById(obj).style.visibility='hidden';" + "}" + " </SCRIPT>\n");
			bw.write("<BODY><TABLE BORDER=0 CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");
			bw.write("<TABLE BORDER=0 BGCOLOR=BLACK CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");

			bw.write("<TR><TD BGCOLOR=#66699 WIDTH=27%>"
					+ "<FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Suite Name:</B></FONT></TD>"
					+ "<TD COLSPAN=6 BGCOLOR=#66699><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>" + testCase
					+ "</B></FONT></TD></TR>");

			bw.write("<HTML><BODY><TABLE BORDER=1 CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");

			bw.write("<TR COLS=3>"
					+ "<TD BGCOLOR=#FFCC99  WIDTH=3%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>CycleRegression</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=21%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Status</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Execution Time</B></FONT></TD>");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void updateStartTest(String reportPath, String cycleRegression, ReportingStatus reportingStatus,
			String startTime) {

		String s = "";
		String str_time;
		// String[] str_rep = new String[2];
		Date exec_time = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		str_time = dateFormat.format(exec_time);

		String cyclePath = "cycleReport\\" + cycleRegression + startTime + ".html";

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath, true))) {

			if (reportingStatus.getCurStatus() == Status.PASS) {
				bw.write("<TR COLS=4><TD BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE=VERDANA SIZE=2>"

						+ "<a onclick=\"toogleIframe('cycleFrame')\">"
						+ (StringUtils.isBlank(cycleRegression) ? s : cycleRegression) + "</a>" + "</FONT></TD>"
						+ "<TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2 COLOR = GREEN>" + "PASS"
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>" + str_time
						+ "</FONT></TD></TR>");

			} else if (reportingStatus.getCurStatus() == Status.FAIL) {

				bw.write("<TR COLS=3><TD BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE=VERDANA SIZE=2>"

						+ "<a onclick=\"toogleIframe('cycleFrame')\">"
						+ (StringUtils.isBlank(cycleRegression) ? s : cycleRegression) + "</a>" + "</FONT></TD>"
						+ "<TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2 COLOR = RED>" + "FAIL"
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>" + str_time
						+ "</FONT></TD></TR>");

				if (StringUtils.isNotBlank(reportingStatus.getDescription())) {
					bw.write(
							"<TR COLS=3><th colspan= 3 BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE='WINGDINGS 2' SIZE=3 COLOR=RED><div align=left></FONT><FONT FACE=VERDANA SIZE=2 COLOR = RED>"
									+ reportingStatus.getDescription() + "</div></th></FONT></TR>");
				}

			}

			bw.write(
					"<TR COLS=3><th colspan= 3 BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE='WINGDINGS 2' SIZE=3><div  id='cycleFrame' align=left>"
							+ "<iframe width=100% onload=\"resizeIframe(this)\" frameborder=\"0\" src=\"" + cyclePath
							+ "\" ></iframe>" + "</div></FONT></th></TR>");

		} catch (Exception e1) {
			System.out.println("Start Report will not be printed. Check the file path.  " + e1);
		}
	}

	public static void createTestSuiteHtml(String reportPath, String testCase) {

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath))) {
			bw.write("<HTML><BODY><TABLE BORDER=0 CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");
			bw.write("<TABLE BORDER=0 BGCOLOR=BLACK CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");

			bw.write("<TR><TD BGCOLOR=#66699 WIDTH=27%>"
					+ "<FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Suite Name:</B></FONT></TD>"
					+ "<TD COLSPAN=6 BGCOLOR=#66699><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>" + testCase
					+ "</B></FONT></TD></TR>");

			bw.write("<HTML><BODY><TABLE BORDER=1 CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");

			bw.write("<TR COLS=5>"
					+ "<TD BGCOLOR=#FFCC99  WIDTH=3%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>StepTableReference</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=20% ><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Sheet to Run</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99  WIDTH=23%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Variable File</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=21%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Status</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Execution Time</B></FONT></TD>");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void updateSuiteReport(String reportPath, CycleSuiteExcelDTO suiteDto,
			ReportingStatus reportingStatus, String startTime) {

		String s = "";
		String str_time;
		// String[] str_rep = new String[2];
		Date exec_time = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		str_time = dateFormat.format(exec_time);

		String p1 = "../";
		if (suiteDto.getTableReference().toLowerCase().startsWith("step")) {
			p1 = p1 + "stepReport\\";
		} else {
			p1 = p1 + "suiteReport\\";
		}
		String suitePath = p1 + suiteDto.getTableReference() + "(" + suiteDto.getSheetToRun() + ")" + startTime
				+ ".html";

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath, true))) {

			if (reportingStatus.getCurStatus() == Status.PASS) {
				bw.write("<TR COLS=5><TD BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE=VERDANA SIZE=2>"

						+ "<a>" + (suiteDto.getTableReference() == null ? s : suiteDto.getTableReference()) + "</a>"
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=17%><FONT FACE=VERDANA SIZE=2>"
						+ (suiteDto.getSheetToRun() + 1)
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (suiteDto.getVariableFile() == null ? s : suiteDto.getVariableFile())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2 COLOR = GREEN>" + "PASS"
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>" + str_time
						+ "</FONT></TD></TR>");
			} else if (reportingStatus.getCurStatus() == Status.FAIL) {

				bw.write("<TR COLS=5><TD BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE=VERDANA SIZE=2>"

						+ "<a>" + (suiteDto.getTableReference() == null ? s : suiteDto.getTableReference()) + "</a>"
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=17%><FONT FACE=VERDANA SIZE=2>"
						+ (suiteDto.getSheetToRun() + 1)
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (suiteDto.getVariableFile() == null ? s : suiteDto.getVariableFile())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2 COLOR = RED>" + "FAIL"
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>" + str_time
						+ "</FONT></TD></TR>");

				if (StringUtils.isNotBlank(reportingStatus.getDescription())) {
					bw.write(
							"<TR COLS=5><th colspan= 5 BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE='WINGDINGS 2' SIZE=3 COLOR=RED><div align=left></FONT><FONT FACE=VERDANA SIZE=2 COLOR = RED>"
									+ reportingStatus.getDescription() + "</div></th></FONT></TR>");
				}

			}

			bw.write(
					"<TR COLS=5><th colspan= 5 BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE='WINGDINGS 2' SIZE=3><div  align=left>"
							+ "<iframe width=100% onload=\"resizeIframe(this)\" frameborder=\"0\" src=\"" + suitePath
							+ "\" ></iframe>" + "</div></FONT></th></TR>");

		} catch (Exception e1) {
			System.out.println("Cycle/Suite Report will not be printed. Check the file path.  " + e1);
		}
	}

	public static void createTestStepHtml(String reportPath, String testCase) {

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath))) {
			bw.write("<HTML><BODY><TABLE BORDER=0 CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");
			bw.write("<HTML><BODY><TABLE BORDER=0 CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");
			bw.write("<TABLE BORDER=0 BGCOLOR=BLACK CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");

			bw.write("<TR><TD BGCOLOR=#66699 WIDTH=27%>"
					+ "<FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Test Case Name:</B></FONT></TD>"
					+ "<TD COLSPAN=6 BGCOLOR=#66699><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>" + testCase
					+ "</B></FONT></TD></TR>");

			bw.write("<HTML><BODY><TABLE BORDER=1 CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");

			bw.write("<TR COLS=7>"
					+ "<TD BGCOLOR=#FFCC99  WIDTH=3%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Action</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=20% ><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Component</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99  WIDTH=23%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Input</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=21%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Output</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=21%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Exp Output</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Execution Time</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=5%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Status</B></FONT></TD>");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void writeInReport(String reportPath, String str) {

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath, true))) {

			bw.write("<TR COLS=7>" + "<TD BGCOLOR=#e0e0e0 colspan=7><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>" + str
					+ "</B></FONT></TD>");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void updateStepReport(String reportPath, StepExcelDTO stepDto, ReportingStatus reportingStatus) {

		String s = "";
		String str_time;
		// String[] str_rep = new String[2];
		Date exec_time = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		str_time = dateFormat.format(exec_time);

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath, true))) {

			if (reportingStatus.getCurStatus() == Status.PASS) {
				bw.write("<TR COLS=6><TD BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE=VERDANA SIZE=2>"

						+ (stepDto.getAction() == null ? s : stepDto.getAction())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=17%><FONT FACE=VERDANA SIZE=2>"
						+ (stepDto.getComponent() == null ? s : stepDto.getComponent())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (stepDto.getInput() == null ? s : stepDto.getInput())
						+ "</FONT></TD></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (stepDto.getOutput() == null ? s : stepDto.getOutput())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (stepDto.getExpected() == null ? s : stepDto.getExpected())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>" + str_time
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2 COLOR = GREEN>"
						+ "Passed" + "</FONT></TD></TR>");

				if (StringUtils.isNotBlank(reportingStatus.getDescription())) {
					bw.write(
							"<TR COLS=6><th colspan= 6 BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE='WINGDINGS 2' SIZE=3 COLOR=GREEN><div align=left></FONT><FONT FACE=VERDANA SIZE=2 COLOR = GREEN>"
									+ reportingStatus.getDescription() + "</div></th></FONT></TR>");
				}

			} else if (reportingStatus.getCurStatus() == Status.FAIL) {

				bw.write("<TR COLS=6><TD BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE=VERDANA SIZE=2>"

						+ (stepDto.getAction() == null ? s : stepDto.getAction())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=17%><FONT FACE=VERDANA SIZE=2>"
						+ (stepDto.getComponent() == null ? s : stepDto.getComponent())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (stepDto.getInput() == null ? s : stepDto.getInput())
						+ "</FONT></TD></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (stepDto.getOutput() == null ? s : stepDto.getOutput())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (stepDto.getExpected() == null ? s : stepDto.getExpected())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>" + str_time
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2 COLOR = RED>" + "Failed"
						+ "</FONT></TD></TR>");

				bw.write(
						"<TR COLS=6><th colspan= 6 BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE='WINGDINGS 2' SIZE=3 COLOR=RED><div align=left></FONT><FONT FACE=VERDANA SIZE=2 COLOR = RED>"
								+ reportingStatus.getDescription() + "</div></th></FONT></TR>");

			}
		} catch (Exception e1) {
			System.out.println("Step Report will not be printed. Check the file path.  " + e1);
		}
	}
}