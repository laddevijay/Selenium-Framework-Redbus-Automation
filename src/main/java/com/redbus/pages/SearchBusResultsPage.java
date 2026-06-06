package com.redbus.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.redbus.base.BasePage;
import com.redbus.utils.JavaScriptExecutorUtil;

public class SearchBusResultsPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(SearchBusResultsPage.class);

    // -------------------- Locators --------------------

    private By busResultsContainer =
            By.xpath("//div[contains(@class,'busesFoundText')]");

    private By filterSection =
            By.xpath("//nav[contains(@class,'filterSection')]");
    private  By buses = By.xpath("//div[contains(@class,'busesFoundText')]");
    private By endOfList = By.xpath("//span[contains(@class,'endText')]");
    
    private By busList = By.xpath("//li[contains(@class,'tupleWrapper')]");
    private By busNames = By.xpath("//div[contains(@class, 'travelsName')]");

    
    JavaScriptExecutorUtil js = new  JavaScriptExecutorUtil(driver);
    // -------------------- Page Validations --------------------

    public boolean isPageLoaded() {

        logger.info("Validating Search Results Page");

        boolean resultsVisible = action.isDisplayed(busResultsContainer);
        boolean filterVisible = action.isDisplayed(filterSection);

        logger.debug("Results visible: {}", resultsVisible);
        logger.debug("Filter visible: {}", filterVisible);

        return resultsVisible && filterVisible;
    }

    public String getCurrentUrl() {
        String url = action.getCurrentUrl();
        logger.info("Current URL: {}", url);
        return url;
    }

    // -------------------- Additional Useful Methods --------------------

    public boolean isResultsVisible() {
        return action.isDisplayed(busResultsContainer);
    }

    public boolean isFilterSectionVisible() {
        return action.isDisplayed(filterSection);
    }

    public String getResultsText() {
        return action.getText(busResultsContainer);
    }
    
    public void noOfBuses(String fromCity, String toCity) {
    	String result = action.getText(buses);
    	System.out.println("no of Buses from "+fromCity+" to "+toCity+" - " +result);
    }
    
  
    
    public void printAllBuses() {
    	
    	action.scrollToElement(buses);
    	
        while (true) {
            try {
                // Check if end of list is visible
                if (driver.findElement(endOfList).isDisplayed()) {
                    break;
                }
            } catch (NoSuchElementException e) {
                // End not yet visible → continue scrolling
            }

            // Scroll down
            js.scrollBy(0,1000);

            // Small wait for lazy loading
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    	
    	List<WebElement> allBuses = driver.findElements(busList);
    	
    	for(WebElement bus: allBuses ) {
    		String busName= bus.findElement(busNames).getText();
    		System.out.println(busName);
    	}
    	
    }
}