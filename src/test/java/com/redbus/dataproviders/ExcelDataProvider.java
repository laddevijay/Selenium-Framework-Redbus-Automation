package com.redbus.dataproviders;

import org.testng.annotations.DataProvider;

import com.redbus.utils.ExcelUtil;

import java.util.List;
import java.util.Map;

/*

@Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
public void createMemberTest(Map<String, String> data) {

    String name = data.get("name");
    String email = data.get("email");
    String phone = data.get("phone");

    // Use in test
}*/
public class ExcelDataProvider {

	private static final String TEST_DATA_PATH = System.getProperty("user.dir")
			+ "/src/test/resources/testdata/TestData.xlsx";

	// -------------------- Generic DataProvider --------------------

	@DataProvider(name = "excelData", parallel = true)
	public static Object[][] getExcelData() {

		List<Map<String, String>> data = ExcelUtil.readExcel(TEST_DATA_PATH, "Sheet1");

		Object[][] result = new Object[data.size()][1];

		for (int i = 0; i < data.size(); i++) {
			result[i][0] = data.get(i); // each row as Map
		}

		return result;
	}

	// -------------------- Sheet Specific (Optional) --------------------

	@DataProvider(name = "userData", parallel = true)
	public static Object[][] getUserData() {

		return convertToDataProvider(ExcelUtil.readExcel(TEST_DATA_PATH, "Users"));
	}

	@DataProvider(name = "bookingData", parallel = true)
	public static Object[][] getBookingData() {

		return convertToDataProvider(ExcelUtil.readExcel(TEST_DATA_PATH, "Bookings"));
	}

	// -------------------- Reusable Converter --------------------

	private static Object[][] convertToDataProvider(List<Map<String, String>> list) {

		Object[][] data = new Object[list.size()][1];

		for (int i = 0; i < list.size(); i++) {
			data[i][0] = list.get(i);
		}

		return data;
	}
}
