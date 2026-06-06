package com.redbus.actions;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;

public interface Actions {

    // -------------------- Browser Info --------------------

    String getTitle();
    String getCurrentUrl();
    String getPageSource();

    // -------------------- Element Handling --------------------

    WebElement find(By locator);
    List<WebElement> findAll(By locator);

    // -------------------- Window Handling --------------------

    String getWindowHandle();
    Set<String> getWindowHandles();

    // -------------------- Basic UI Actions --------------------

    void click(By locator);
    void type(By locator, String value);

    boolean isDisplayed(By locator);
    boolean isEnabled(By locator);

    String getText(By locator);
    String getAttribute(By locator, String attributeName);
    
    // -------------------- Scrolling --------------------

    void scrollToElement(By locator);
}