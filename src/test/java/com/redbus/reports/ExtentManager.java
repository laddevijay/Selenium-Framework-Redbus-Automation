package com.redbus.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExtentManager {

	private static ExtentReports extent;

	private ExtentManager() {
	}

	public static ExtentReports getInstance() {

		if (extent == null) {

			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
			String path = System.getProperty("user.dir") + File.separator + "reports" + File.separator
					+ "ExtentReport"+timestamp+".html";

			File dir = new File(path).getParentFile();
			if (!dir.exists())
				dir.mkdirs();

			ExtentSparkReporter spark = new ExtentSparkReporter(path);

			spark.config().setReportName("RedBus Automation Report");
			spark.config().setDocumentTitle("Execution Results");
			spark.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.STANDARD);

			extent = new ExtentReports();
			extent.attachReporter(spark);

			extent.setSystemInfo("Framework", "Selenium + TestNG");
			extent.setSystemInfo("Author", "QA Team");
			extent.setSystemInfo("Environment", System.getProperty("env", "QA"));
			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
		}

		return extent;
	}

	public static void flushReport() {
		if (extent != null) {
			extent.flush();
		}
	}
}