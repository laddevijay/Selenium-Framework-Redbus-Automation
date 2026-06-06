package com.redbus.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import org.openqa.selenium.*;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import com.redbus.driver.DriverManager;

public final class ScreenshotUtil {

	private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);

	private static final String BASE_DIR = System.getProperty("user.dir") + File.separator + "test-output" + File.separator
			+ "screenshots";

	private static final String NORMAL_DIR = BASE_DIR + File.separator + "normal";
	private static final String FULL_DIR = BASE_DIR + File.separator + "fullpage";
	private static final String ELEMENT_DIR = BASE_DIR + File.separator + "elements";

	private ScreenshotUtil() {
	}

	private static String getTimestamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
	}

	// -------------------- Normal Screenshot --------------------

	public static String captureScreenshot(String testName) {
	    WebDriver driver = DriverManager.getDriver();
	    if (driver == null) {
	        logger.error("Driver is null. Cannot capture screenshot.");
	        return "";
	    }
	    String path = NORMAL_DIR + File.separator + testName + "_" + getTimestamp() + ".png";
	    File dest = null;
	    try {
	        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        dest = new File(path);
	        FileUtils.copyFile(src, dest);
	        logger.info("Normal screenshot saved: {}", path);
	    } catch (IOException e) {
	        logger.error("Failed to capture screenshot", e);
	        return "";
	    }
	    return dest != null ? dest.getAbsolutePath() : "";
	}
	// -------------------- Full Page Screenshot --------------------

	public static void captureFullPage(String testName) {
		WebDriver driver = DriverManager.getDriver();
		if (driver == null) {
			logger.error("Driver is null. Cannot capture full page screenshot.");
			return;
		}

		try {
			String path = FULL_DIR + File.separator + testName + "_" + getTimestamp() + ".png";
			Screenshot screenshot = new AShot()
					.shootingStrategy(ShootingStrategies.viewportPasting(100))
					.takeScreenshot(driver);
			ImageIO.write(screenshot.getImage(), "PNG", new File(path));

			logger.info("Full page screenshot saved: {}", path);
		} catch (IOException e) {
			logger.error("Failed to capture full page screenshot", e);
		}
	}

	// -------------------- Element Screenshot --------------------

	public static void captureElement(String testName, WebElement element) {
		WebDriver driver = DriverManager.getDriver();
		if (driver == null || element == null) {
			logger.error("Driver or element is null. Cannot capture element screenshot.");
			return;
		}

		try {
			String path = ELEMENT_DIR + File.separator + testName + "_" + getTimestamp() + ".png";
			File src = element.getScreenshotAs(OutputType.FILE);
			File dest = new File(path);
			FileUtils.copyFile(src, dest);

			logger.info("Element screenshot saved: {}", path);
		} catch (IOException e) {
			logger.error("Failed to capture element screenshot", e);
		}
	}
}