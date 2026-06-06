package com.redbus.driver;

import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages WebDriver instances using ThreadLocal (parallel-ready)
 */
public final class DriverManager {	

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(DriverManager.class);

    private DriverManager() {}

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
        logger.info("Driver set for thread: {}", Thread.currentThread().getName());
    }

    public static WebDriver getDriver() {
        WebDriver webDriver = driver.get();

        if (webDriver == null) {
            throw new IllegalStateException("WebDriver is not initialized for this thread.");
        }

        return webDriver;
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();

        if (webDriver != null) {
        	logger.info("Driver set for thread: {}", Thread.currentThread().getName());
            webDriver.quit();
            driver.remove(); // important cleanup
        }
    }
}