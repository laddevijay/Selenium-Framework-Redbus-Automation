package com.redbus.actions;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.redbus.config.ConfigReader;
import com.redbus.utils.JavaScriptExecutorUtil;
import com.redbus.utils.WaitUtil;

public class ActionDriver implements Actions {

    private final WebDriver driver;
    private final JavaScriptExecutorUtil js;

    private static final Logger logger = LogManager.getLogger(ActionDriver.class);

    // Default timeout (centralized)
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(ConfigReader.getExplicitWait());

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        this.js = new JavaScriptExecutorUtil(driver);
    }

    // -------------------- Browser Info --------------------

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    // -------------------- Element Handling --------------------

    @Override
    public WebElement find(By locator) {
        return driver.findElement(locator);
    }

    @Override
    public List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    // -------------------- Window Handling --------------------

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    // -------------------- Actions --------------------

    @Override
    public void click(By locator) {
        try {
            WebElement element = WaitUtil.waitForClickable(driver, locator, DEFAULT_TIMEOUT);
            element.click();
            js.highlight(element);

        } catch (Exception e) {

            logger.warn("Normal click failed. Trying JS click: {}", locator);

            try {
                WebElement element = driver.findElement(locator);
                js.click(element);
                js.highlight(element);

            } catch (Exception ex) {
                fail("Click failed: " + locator, ex);
            }
        }
    }

    @Override
    public void type(By locator, String value) {
        try {
            WebElement element = WaitUtil.waitForVisible(driver, locator, DEFAULT_TIMEOUT);
            element.clear();
            element.sendKeys(value);

        } catch (Exception e) {

            logger.warn("Normal type failed. Trying JS type: {}", locator);

            try {
                WebElement element = driver.findElement(locator);
                js.type(element, value);

            } catch (Exception ex) {
                fail("Type failed: " + locator, ex);
            }
        }
    }

    @Override
    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public boolean isEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public String getText(By locator) {
        try {
            return WaitUtil.waitForVisible(driver, locator, DEFAULT_TIMEOUT).getText();
        } catch (Exception e) {
            fail("GetText failed: " + locator, e);
            return null;
        }
    }

    @Override
    public String getAttribute(By locator, String attributeName) {
        try {
            return driver.findElement(locator).getAttribute(attributeName);
        } catch (Exception e) {
            fail("GetAttribute failed: " + locator, e);
            return null;
        }
    }

    // -------------------- Scrolling --------------------

    @Override
    public void scrollToElement(By locator) {
        try {
            js.scrollTo(driver.findElement(locator));
        } catch (Exception e) {
            fail("Scroll failed: " + locator, e);
        }
    }

    // -------------------- Failure Handler --------------------

    private void fail(String message, Exception e) {
        logger.error(message, e);
        throw new RuntimeException(message, e);
    }
}