package com.redbus.pages;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.redbus.base.BasePage;
import com.redbus.utils.WaitUtil;

public class HomePage extends BasePage {

	private static final Logger logger = LogManager.getLogger(HomePage.class);

	// -------------------- Locators --------------------

	private final By logo = By.xpath("//img[contains(@alt,'redBus')]");
	private final By fromCityWrapper = By.xpath("//div[contains(@class,'srcDestWrapper')]");
	private final By toCityField = By.id("destinput");
	private final By datePicker = By.xpath("//div[contains(@class,'dateWrapper')]");
	private final By searchButton = By.xpath("//button[contains(@class,'searchButtonWrapper')]");

	private final By footerSection = By.xpath("//nav[@aria-label='Footer links']");
	private final By facebookIcon = By.xpath("//a[contains(@href,'facebook.com')]");
	private final By twitterIcon = By.xpath("//a[contains(@href,'twitter.com')]");
	private final By instagramIcon = By.xpath("//a[contains(@href,'instagram.com')]");
	private final By youtubeIcon = By.xpath("//a[contains(@href,'youtube.com')]");

	private final By suggestionContainer = By.xpath("//div[@class='searchCategory___ab3496']");
	private final By suggestionOptions = By.xpath("//div[@class='listHeader___19289f']");
	private By socialLinksWrapper = By.xpath("//ul[contains(@class, 'socialProfilesWrapper')]/li/a");
	
	private By fromCitySuggestion = By.xpath("//div[contains(@class, 'searchSuggestionWrapper')]/div");
	private final By suggestionFromOptions = By.xpath("//div[@role='heading' and contains(@class, 'listHeader')]");

	// -------------------- Page Validations --------------------

	public boolean isHomePageLoaded() {
		logger.info("Checking Home Page loaded");
		return action.isDisplayed(logo);
	}

	public boolean areSearchFieldsVisible() {
		return action.isDisplayed(fromCityWrapper) && action.isDisplayed(toCityField) && action.isDisplayed(datePicker);
	}

	public boolean isSearchButtonEnabled() {
		return action.isDisplayed(searchButton) && action.isEnabled(searchButton);
	}

	public String getPageTitle() {
		return action.getTitle();
	}

	public String getCurrentUrl() {
		return action.getCurrentUrl();
	}

	// -------------------- Search Actions --------------------

	public void enterFromCity(String fromCity) {
		logger.info("Entering FROM city: {}", fromCity);

		action.click(fromCityWrapper);
		driver.switchTo().activeElement().sendKeys(fromCity);

		selectCityFromSuggestions(fromCity);
	}

	public void enterToCity(String toCity) {
		logger.info("Entering TO city: {}", toCity);

		driver.switchTo().activeElement().sendKeys(toCity);

		selectCityFromSuggestions(toCity);
	}

	public void clickSearch() {
		logger.info("Clicking Search button");
		action.click(searchButton);
	}

	public SearchBusResultsPage searchBus(String fromCity, String toCity) {

		enterFromCity(fromCity);
		enterToCity(toCity);
		clickSearch();

		return new SearchBusResultsPage();
	}

	public boolean validateSocialLinks() {

		action.scrollToElement(footerSection);
		
		boolean allLinksValid = true;

		List<WebElement> socialHandles = driver.findElements(socialLinksWrapper);

		for (WebElement socialHandle : socialHandles) {
			String url = socialHandle.getAttribute("href");
			if (!isLinkValid(url)) {
				System.out.println("Broken link: " + url);
				allLinksValid = false;
			}
		}
		return allLinksValid;
	}

	public boolean isLinkValid(String urlLink) {

		if (urlLink == null || urlLink.isEmpty()) {
			return false;
		}

		HttpURLConnection connection = null;

		try {
			URL url = new URL(urlLink);
			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.setInstanceFollowRedirects(true);
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.connect();

			int statusCode = connection.getResponseCode();
			//System.out.println(statusCode);

			boolean result;
			if (statusCode >= 400 && statusCode != 999)
				result = false;
			else
				result = true;

			return result;

		} catch (IOException e) {
			System.out.println("Exception for URL: " + urlLink);
			return false;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private void selectCityFromSuggestions(String cityName) {

		logger.info("Selecting city from suggestions: {}", cityName);

		Wait<WebDriver> wait = WaitUtil.getFluentWait(driver, 30, 5);
		List<WebElement> optionsParent = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(fromCitySuggestion, 2));
		
		WebElement suggestion  = optionsParent.get(0);
		List<WebElement> options = suggestion.findElements(suggestionFromOptions);
		for (WebElement option : options) {
			String text = option.getText();
			
			if (text.contains(cityName)) {
				logger.info("Selected: {}", text);
				option.click();
				return;
			}
		}

		throw new RuntimeException("City not found in suggestions: " + cityName);
	}

	// -------------------- Footer Actions --------------------

	public boolean isFooterVisible() {
		action.scrollToElement(footerSection);
		return action.isDisplayed(footerSection);
	}

	public Map<String, String> getSocialMediaLinks() {

		logger.info("Fetching social media links");

		action.scrollToElement(footerSection);

		Map<String, String> links = new HashMap<>();

		links.put("facebook", action.getAttribute(facebookIcon, "href"));
		links.put("twitter", action.getAttribute(twitterIcon, "href"));
		links.put("instagram", action.getAttribute(instagramIcon, "href"));
		links.put("youtube", action.getAttribute(youtubeIcon, "href"));

		return links;
	}

	// -------------------- Utility --------------------

	public boolean isElementDisplayed(By locator) {
		return action.isDisplayed(locator);
	}

	public String getElementText(By locator) {
		return action.getText(locator);
	}
}