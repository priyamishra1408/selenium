package com.epsilon.elsautomation.dto;

public class CycleSuiteExcelDTO {
	
	private String tableReference;
	
	private String execute;
	
	private int sheetToRun;
	
	private String variableFile;
	
	private String variableName;
	
	private String purpose;

	public String getTableReference() {
		return tableReference;
	}

	public void setTableReference(String tableReference) {
		this.tableReference = tableReference;
	}

	public String getExecute() {
		return execute;
	}

	public void setExecute(String execute) {
		this.execute = execute;
	}

	public int getSheetToRun() {
//		if(sheetToRun>0){
//			sheetToRun = sheetToRun-1;
//		}
		return sheetToRun;
	}

	public void setSheetToRun(int sheetToRun) {
		this.sheetToRun = sheetToRun;
	}

	public String getVariableFile() {
		return variableFile;
	}

	public void setVariableFile(String variableFile) {
		this.variableFile = variableFile;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	@Override
	public String toString() {
		return "CycleSuiteExcelDTO [tableReference=" + tableReference + ", execute=" + execute + ", sheetToRun="
				+ sheetToRun + ", variableFile=" + variableFile + ", variableName=" + variableName + ", purpose="
				+ purpose + "]";
	}
	
}
