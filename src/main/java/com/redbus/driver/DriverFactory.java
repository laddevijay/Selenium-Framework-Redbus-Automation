package com.redbus.driver;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.redbus.config.ConfigReader;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Responsible for initializing WebDriver based on config
 */
public final class DriverFactory {	

	private static final Logger logger = LogManager.getLogger(DriverFactory.class);

	private DriverFactory() {
	}

	public static void initDriver() {

		logger.info("===== Driver Initialization Started =====");

		String browser = ConfigReader.getBrowser().toLowerCase();
		logger.info("Browser: {}", browser);

		WebDriver driver;

		switch (browser) {

		case "chrome":
			logger.info("Launching Chrome browser");
			WebDriverManager.chromedriver().setup();

			String downloadPath = System.getProperty("user.dir") + "/downloads";

			// Ensure directory exists (CI safe)
			File dir = new File(downloadPath);
			if (!dir.exists() && dir.mkdirs()) {
				logger.info("Created download directory: {}", downloadPath);
			}

			Map<String, Object> prefs = new HashMap<>();
			prefs.put("download.default_directory", downloadPath);
			prefs.put("download.prompt_for_download", false);
			prefs.put("safebrowsing.enabled", true);

			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setExperimentalOption("prefs", prefs);
			chromeOptions.addArguments("--start-maximized");
			chromeOptions.addArguments("--force-device-scale-factor=0.85");
			chromeOptions.addArguments("--high-dpi-support=0.85");

			logger.info("Download directory: {}", downloadPath);
			driver = new ChromeDriver(chromeOptions);
			break;

		case "edge":
			logger.info("Launching Edge browser");

			WebDriverManager.edgedriver().setup();
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.addArguments("--start-maximized");

			driver = new EdgeDriver(edgeOptions);
			break;

		case "firefox":
			logger.info("Launching Firefox browser");
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;

		default:
			logger.error("Unsupported browser: {}", browser);
			throw new IllegalArgumentException("Unsupported browser: " + browser);
		}

		DriverManager.setDriver(driver);
		setTimeouts(driver);

		logger.info("Driver initialized successfully");
		logger.info("===== Driver Initialization Completed =====");
	}

	// -------------------- Common Config --------------------

	private static void setTimeouts(WebDriver driver) {

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));

		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));

		logger.info("Timeouts configured successfully");
	}
}