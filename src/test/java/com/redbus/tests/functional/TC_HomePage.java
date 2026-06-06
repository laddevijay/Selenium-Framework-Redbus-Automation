package com.redbus.tests.functional;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.redbus.base.BaseTest;
import com.redbus.pages.HomePage;
import com.redbus.pages.SearchBusResultsPage;

public class TC_HomePage extends BaseTest {

	private HomePage homePage;
	private SearchBusResultsPage searchResultsPage;

	@BeforeMethod
	public void initPage() {
		homePage = new HomePage();
	}

	@Test(description = "Validate homepage loads successfully")
	public void testHomePageLoad() {

		logger.info("Validating homepage load");

		Assert.assertTrue(homePage.isHomePageLoaded(), "Homepage did not load successfully");
	}

	@Test(description = "Validate search fields are visible")
	public void testSearchFieldsPresence() {

		logger.info("Validating search fields");

		Assert.assertTrue(homePage.areSearchFieldsVisible(), "Search fields are not visible");
	}

	@Test(description = "Validate social media links in works")
	public void validateSocialLinks() {

		logger.info("Validating social media links");

		boolean result = homePage.validateSocialLinks();

		Assert.assertTrue(result);
	}

	@Test(description = "Validate footer section is visible")
	public void validateFooterSection() {

		logger.info("Validating footer");

		Assert.assertTrue(homePage.isFooterVisible(), "Footer is not visible");
	}

	@Test(description = "Verify navigation to Search Results Page")
	public void verifyNavigationToSearchResultsPage() {

		logger.info("Validating search navigation");

		searchResultsPage = homePage.searchBus("Pune", "Mumbai");

		Assert.assertTrue(searchResultsPage.isPageLoaded(), "Search Results Page not loaded");
	}
}