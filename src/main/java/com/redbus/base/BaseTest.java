package com.redbus.base;

import java.awt.Desktop;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

import com.redbus.config.ConfigReader;
import com.redbus.driver.DriverFactory;
import com.redbus.driver.DriverManager;

public class BaseTest {

	protected WebDriver driver;
	protected Logger logger = LogManager.getLogger(getClass());

	@BeforeMethod
	public void setUp() {

		logger.info("========== TEST STARTED ==========");
		logger.info("Browser: {}", ConfigReader.getBrowser());
		logger.info("URL: {}", ConfigReader.getBaseUrl());

		// Initialize driver
		DriverFactory.initDriver();
		driver = DriverManager.getDriver();

		// Navigate to application
		driver.get(ConfigReader.getBaseUrl());
	}

	@AfterMethod
	public void tearDown(ITestResult result) {

		String testName = result.getName();

		switch (result.getStatus()) {

		case ITestResult.FAILURE:
			logger.error("Test FAILED: {}", testName);
			break;

		case ITestResult.SUCCESS:
			logger.info("Test PASSED: {}", testName);
			break;	

		case ITestResult.SKIP:
			logger.warn("Test SKIPPED: {}", testName);
			break;

		default:
			logger.warn("Test UNKNOWN STATUS: {}", testName);
		}

		logger.info("========== TEST ENDED ==========");

		DriverManager.quitDriver();
	}


	public void openLatestExtentReport() {

		try {
			String reportDirPath = System.getProperty("user.dir") + "/reports/";

			File dir = new File(reportDirPath);

			// Get all HTML files
			File[] reports = dir.listFiles((d, name) -> name.endsWith(".html"));

			if (reports == null || reports.length == 0) {
				System.out.println("No Extent report found.");
				return;
			}

			// Sort by last modified (latest first)
			File latestReport = Arrays.stream(reports)
					.max(Comparator.comparingLong(File::lastModified))
					.orElse(null);

			if (latestReport != null && latestReport.exists()) {
				Desktop.getDesktop().browse(latestReport.toURI());
				System.out.println("Opening report: " + latestReport.getName());
			}

		} catch (Exception e) {
			System.out.println("Unable to open Extent Report: " + e.getMessage());
		}
	}
}