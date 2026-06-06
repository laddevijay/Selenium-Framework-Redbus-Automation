package com.redbus.base;

import org.openqa.selenium.WebDriver;

import com.redbus.driver.DriverManager;
import com.redbus.actions.ActionDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasePage {

    protected final WebDriver driver;
    protected final ActionDriver action;

    protected final Logger logger = LogManager.getLogger(getClass());

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.action = new ActionDriver(driver);
    }
}