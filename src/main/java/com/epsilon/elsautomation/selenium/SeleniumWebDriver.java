package com.epsilon.elsautomation.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class SeleniumWebDriver {

	private static SeleniumWebDriver seleniumWebDriver = new SeleniumWebDriver();
	
	private WebDriver webDriver;
	
	public WebDriver getWebdriver()
	{
	return webDriver;
	
	}


	private SeleniumWebDriver() {
	}

	public static SeleniumWebDriver getInstance() {
		if (seleniumWebDriver != null)
			return seleniumWebDriver;
		else
			return new SeleniumWebDriver();
	}

	public void launchWebDriver(String browser, String url) throws Exception {
		WebDriver webdriver = null;
		
		browser = browser.replace("\"", "");
		url = url.replace("\"", "");

		if (browser.equalsIgnoreCase("firefox")) {
			// create firefox instance
			webdriver = new FirefoxDriver();
		}
		// Check if parameter passed as 'chrome'
		else if (browser.equalsIgnoreCase("chrome")) {
			// set path to chromedriver.exe
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\prmishra\\Downloads\\chromedriver.exe");
			// create chrome instance
			webdriver = new ChromeDriver();
		}
		// Check if parameter passed as 'IE'
		else if (browser.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", ".\\IEDriverServer.exe");
			// create IE instance
			webdriver = new InternetExplorerDriver();
		} else {
			// If no browser passed throw exception
			throw new Exception("Browser is not correct");
		}
		
		webdriver.manage().window().maximize();
		webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		

		this.webDriver = webdriver;

		webDriver.get(url);

	}

}
