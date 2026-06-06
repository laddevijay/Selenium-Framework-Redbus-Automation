package com.redbus.tests.functional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.redbus.base.BaseTest;
import com.redbus.pages.HomePage;
import com.redbus.pages.SearchBusResultsPage;

public class TC_SearchBusResultsPage extends BaseTest {

	private HomePage homePage;
	private SearchBusResultsPage searchResultsPage;

	@BeforeMethod
	public void initPage() {
		homePage = new HomePage();
		searchResultsPage =homePage.searchBus("Pune", "Mumbai");
	}
	
	@Test
	public void testPrintBuses() {
		searchResultsPage.printAllBuses();
	}

}
