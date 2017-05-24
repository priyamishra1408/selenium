package com.epsilon.elsautomation.selenium;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.epsilon.elsautomation.driver.Driver;
import com.epsilon.elsautomation.exception.SeleniumFrameworkException;

public class ElementHelper {

	public WebElement getElementFromProperty(String propertyName, String propertyValue)
			throws SeleniumFrameworkException {

		WebElement webElement = null;
		try {
			WebDriver driver = SeleniumWebDriver.getInstance().getWebdriver();

			if (propertyName != null && propertyValue != null) {
				if (propertyName.equalsIgnoreCase("ID")) {
					webElement = driver.findElement(By.id(propertyValue));
				} else if (propertyName.equalsIgnoreCase("Class")) {
					webElement = driver.findElement(By.className(propertyValue));
				} else if (propertyName.equalsIgnoreCase("CssSelector")) {
					webElement = driver.findElement(By.cssSelector(propertyValue));
				} else if (propertyName.equalsIgnoreCase("LinkText")) {
					webElement = driver.findElement(By.linkText(propertyValue));
				} else if (propertyName.equalsIgnoreCase("Name")) {
					webElement = driver.findElement(By.name(propertyValue));
				} else if (propertyName.equalsIgnoreCase("PartialLinkText")) {

					webElement = driver.findElement(By.partialLinkText(propertyValue));
				} else if (propertyName.equalsIgnoreCase("tagname")) {

					webElement = driver.findElement(By.tagName(propertyValue));
				} else if (propertyName.equalsIgnoreCase("xpath")) {

					webElement = driver.findElement(By.xpath(propertyValue));
				}

			}
			return webElement;

		} catch (Exception e) {
			// TODO: handle exception
			throw new SeleniumFrameworkException("Exeception: " + e.getMessage());
		}

	}

	public List<WebElement> getElementsFromProperty(String propertyName, String propertyValue)
			throws SeleniumFrameworkException {

		List<WebElement> webElements = new ArrayList<WebElement>();
		WebDriver driver = SeleniumWebDriver.getInstance().getWebdriver();

		if (propertyName != null && propertyValue != null) {
			if (propertyName.equalsIgnoreCase("ID")) {
				webElements = driver.findElements(By.id(propertyValue));
			} else if (propertyName.equalsIgnoreCase("Class")) {
				webElements = driver.findElements(By.className(propertyValue));
			} else if (propertyName.equalsIgnoreCase("CssSelector")) {
				webElements = driver.findElements(By.cssSelector(propertyValue));
			} else if (propertyName.equalsIgnoreCase("LinkText")) {
				webElements = driver.findElements(By.linkText(propertyValue));
			} else if (propertyName.equalsIgnoreCase("Name")) {
				webElements = driver.findElements(By.name(propertyValue));
			} else if (propertyName.equalsIgnoreCase("PartialLinkText")) {

				webElements = driver.findElements(By.partialLinkText(propertyValue));
			} else if (propertyName.equalsIgnoreCase("tagname")) {

				webElements = driver.findElements(By.tagName(propertyValue));
			} else if (propertyName.equalsIgnoreCase("xpath")) {

				webElements = driver.findElements(By.xpath(propertyValue));
			}

		}
		return webElements;

	}
}
