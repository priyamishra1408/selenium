package com.epsilon.elsautomation.dto;

public class StepExcelDTO {

	private String Notes;

	private String component;

	private String action;

	private String execute;

	private String input;

	private String output;

	private String expected;

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getExecute() {
		return execute;
	}

	public void setExecute(String execute) {
		this.execute = execute;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	@Override
	public String toString() {
		return "StepExcelDTO [component=" + component + ", action=" + action + ", execute=" + execute + ", input="
				+ input + ", output=" + output + ", expected=" + expected + "]";
	}
	

}
