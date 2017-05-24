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
import com.epsilon.elsautomation.driver.DriverExecutor;
import com.epsilon.elsautomation.dto.CycleSuiteExcelDTO;
import com.epsilon.elsautomation.dto.ReportingDTO;
import com.epsilon.elsautomation.dto.StepExcelDTO;

public class ELSReportingHelper {

	static BufferedWriter bw = null;

	static int idIncrement = 1;

	public static void initReport(String reportPath, String userName) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath))) {
			StringBuilder builder = new StringBuilder();
			builder.append("<html>");
			builder.append("<head>");
			builder.append("<title>TestResult</title>");
			builder.append("</head>");
			builder.append("<body>");
			builder.append("<TABLE BORDER=0 BGCOLOR=BLACK CELLPADDING=3 CELLSPACING=1 WIDTH=100%>");
			builder.append("<TR><TD BGCOLOR=#66699 WIDTH=27%>"
					+ "<FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>User Name:</B></FONT></TD>"
					+ "<TD COLSPAN=6 BGCOLOR=#66699><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>" + userName
					+ "</B></FONT></TD></TR>");
			builder.append("<TR><TD BGCOLOR=#66699 WIDTH=27%>"
					+ "<FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Start Time:</B></FONT></TD>"
					+ "<TD COLSPAN=6 BGCOLOR=#66699><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>"
					+ DriverExecutor.getStartTime() + "</B></FONT></TD></TR>");

			bw.write(builder.toString());

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Failed to create report....");
		}
	}

	public static void addCycleRegression(String reportPath, String regName) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath, true))) {
			StringBuilder sb = new StringBuilder();

			sb.append("<TR>" + "<TD COLSPAN=7 BGCOLOR=#66699><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>" + regName
					+ "</B></FONT></TD></TR>");

			sb.append("<TR COLS=7>"
					+ "<TD BGCOLOR=#FFCC99  WIDTH=3%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Test</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=20% ><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Order Number</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99  WIDTH=23%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Order Name</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=21%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Count</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=21%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Start Time</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=25%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>End Time</B></FONT></TD>"
					+ "<TD BGCOLOR=#FFCC99 WIDTH=5%><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Status</B></FONT></TD>");

			bw.write(sb.toString());

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Failed to update cycle regression name.");
		}
	}

	public static void updateSuiteReport(String reportPath, ReportingDTO dto, ReportingStatus reportingStatus) {

		String s = "";
		String str_time;
		// String[] str_rep = new String[2];
		Date exec_time = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		str_time = dateFormat.format(exec_time);

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath, true))) {

			if (reportingStatus.getCurStatus() == Status.PASS) {
				bw.write("<TR COLS=7><TD BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE=VERDANA SIZE=2>"

						+ (dto.getTestName() == null ? s : dto.getTestName())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=17%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getOrderNum() == null ? s : dto.getOrderNum())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getOrderName() == null ? s : dto.getOrderName())
						+ "</FONT></TD></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getCount() == null ? s : dto.getCount())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getStartTime() == null ? s : dto.getStartTime())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>" + str_time
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2 COLOR = GREEN>"
						+ "Passed" + "</FONT></TD></TR>");

//				if (StringUtils.isNotBlank(reportingStatus.getDescription())) {
//					bw.write(
//							"<TR COLS=7><th colspan= 7 BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE='WINGDINGS 2' SIZE=3 COLOR=GREEN><div align=left></FONT><FONT FACE=VERDANA SIZE=2 COLOR = GREEN>"
//									+ reportingStatus.getDescription() + "</div></th></FONT></TR>");
//				}

			} else if (reportingStatus.getCurStatus() == Status.FAIL) {

				bw.write("<TR COLS=7><TD BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE=VERDANA SIZE=2>"

						+ (dto.getTestName() == null ? s : dto.getTestName())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=17%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getOrderNum() == null ? s : dto.getOrderNum())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getOrderName() == null ? s : dto.getOrderName())
						+ "</FONT></TD></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getCount() == null ? s : dto.getCount())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getStartTime() == null ? s : dto.getStartTime())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>" + str_time
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2 COLOR = RED>" + "Failed"
						+ "</FONT></TD></TR>");

//				bw.write(
//						"<TR COLS=7><th colspan= 7 BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE='WINGDINGS 2' SIZE=3 COLOR=RED><div align=left></FONT><FONT FACE=VERDANA SIZE=2 COLOR = RED>"
//								+ reportingStatus.getDescription() + "</div></th></FONT></TR>");

			}else {

				bw.write("<TR COLS=7><TD BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE=VERDANA SIZE=2>"

						+ (dto.getTestName() == null ? s : dto.getTestName())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=17%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getOrderNum() == null ? s : dto.getOrderNum())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getOrderName() == null ? s : dto.getOrderName())
						+ "</FONT></TD></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getCount() == null ? s : dto.getCount())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>"
						+ (dto.getStartTime() == null ? s : dto.getStartTime())
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2>" + str_time
						+ "</FONT></TD><TD BGCOLOR=#EEEEEE WIDTH=30%><FONT FACE=VERDANA SIZE=2 COLOR = YELLOW>" + "NA"
						+ "</FONT></TD></TR>");

//				bw.write(
//						"<TR COLS=7><th colspan= 7 BGCOLOR=#EEEEEE WIDTH=5%><FONT FACE='WINGDINGS 2' SIZE=3 COLOR=RED><div align=left></FONT><FONT FACE=VERDANA SIZE=2 COLOR = RED>"
//								+ reportingStatus.getDescription() + "</div></th></FONT></TR>");

			}
			
		} catch (Exception e1) {
			System.out.println("Step Report will not be printed. Check the file path.  " + e1);
		}
	}
	
	public static void updateExecutionTime(String reportPath, String totalTime) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath, true))) {
			StringBuilder sb = new StringBuilder();

			sb.append("<TR><TD BGCOLOR=#66699 WIDTH=27%>"
					+ "<FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Total Execution Time:</B></FONT></TD>"
					+ "<TD COLSPAN=6 BGCOLOR=#66699><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>" + totalTime
					+ "</B></FONT></TD></TR>");

			

			bw.write(sb.toString());

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Failed to update cycle regression name.");
		}
	}
}