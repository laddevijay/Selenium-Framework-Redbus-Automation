package com.redbus.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JavaScriptExecutorUtil {

    private final WebDriver driver;
    private final JavascriptExecutor js;

    private static final Logger logger = LogManager.getLogger(JavaScriptExecutorUtil.class);

    public JavaScriptExecutorUtil(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
    }

    // -------------------- BASIC ACTIONS --------------------

    public void click(WebElement element) {
        js.executeScript("arguments[0].click();", element);

    }

    public void type(WebElement element, String text) {
        js.executeScript("arguments[0].value = arguments[1];", element, text);
    }

    public void refreshPage() {
        js.executeScript("history.go(0);");
    }

    public void navigateTo(String url) {
        js.executeScript("window.location='" + url + "';");
    }

    public void scrollBy(int x, int y) {
        js.executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    public void scrollTo(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // -------------------- PAGE INFO --------------------

    public String getTitle() {
        return js.executeScript("return document.title;").toString();
    }

    public String getURL() {
        return js.executeScript("return document.URL;").toString();
    }

    public String getDomain() {
        return js.executeScript("return document.domain;").toString();
    }

    public String getPageHeight() {
        return js.executeScript("return window.innerHeight;").toString();
    }

    public String getPageWidth() {
        return js.executeScript("return window.innerWidth;").toString();
    }

    // -------------------- UI EFFECTS --------------------

    public void highlight(WebElement element) {
        js.executeScript("arguments[0].style.border='2px solid red';", element);
    }

    public void zoom(int percentage) {
        js.executeScript("document.body.style.zoom='" + percentage + "%';");
    }

    public void generateAlert(String message) {
        js.executeScript("alert('" + message + "');");
    }

    public void flash(WebElement element) {

        String originalColor = element.getCssValue("backgroundColor");

        for (int i = 0; i < 5; i++) {
            changeColor("yellow", element);
            changeColor(originalColor, element);
        }
    }

    private void changeColor(String color, WebElement element) {
        js.executeScript("arguments[0].style.backgroundColor='" + color + "';", element);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Flash interrupted");
        }
    }

    // -------------------- ADVANCED SCROLL --------------------

    public void scrollSlowly(int pixels, int steps, int delay) {

        for (int i = 0; i < steps; i++) {
            js.executeScript("window.scrollBy(0, arguments[0]);", pixels);

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Scroll interrupted");
            }
        }
    }
}