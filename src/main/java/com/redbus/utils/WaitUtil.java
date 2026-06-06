package com.redbus.utils;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class WaitUtil {

    private static final Logger logger = LogManager.getLogger(WaitUtil.class);

    private WaitUtil() {
        // prevent instantiation
    }

    // -------------------- Explicit Waits --------------------

    public static WebElement waitForVisible(WebDriver driver, By locator, Duration timeout) {
        logger.info("Waiting for element visibility: {}", locator);
        logger.debug("Entering username and password");
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator, Duration timeout) {
        logger.info("Waiting for element clickable: {}", locator);
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForInvisibility(WebDriver driver, By locator, Duration timeout) {
        logger.info("Waiting for element invisibility: {}", locator);
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // -------------------- Fluent Wait --------------------

    public static Wait<WebDriver> getFluentWait(WebDriver driver, int timeout, int polling) {

        logger.info("Creating FluentWait → timeout: {}, polling: {}", timeout, polling);

        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(polling))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    // -------------------- Custom Wait --------------------

    public static WebElement waitForPresence(WebDriver driver, By locator, Duration timeout) {
        logger.info("Waiting for element presence: {}", locator);
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }
}