package com.epsilon.elsautomation.dto;

public class MappingDTO {
	private String componenet;

	private String propertyName;

	private String propertyValue;

	public String getComponenet() {
		return componenet;
	}

	public void setComponenet(String componenet) {
		this.componenet = componenet;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	@Override
	public String toString() {
		return "MappingDTO [componenet=" + componenet + ", propertyName=" + propertyName + ", propertyValue="
				+ propertyValue + "]";
	}

}
