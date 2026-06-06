package com.redbus.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.redbus.reports.ExtentManager;
import com.redbus.reports.ExtentTestManager;
import com.redbus.utils.ScreenshotUtil;

public class TestListener implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {

		ExtentTest test = ExtentManager.getInstance().createTest(result.getMethod().getMethodName());

		ExtentTestManager.setTest(test);
		test.info("Test Started: " + result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		Integer retryCount = (Integer) result.getAttribute("retryCount");

		if (retryCount != null && retryCount > 0) {
			ExtentTestManager.getTest().pass("Test Passed after retry attempt: " + retryCount);
		} else {
			ExtentTestManager.getTest().pass("Test Passed");
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {

		ExtentTest test = ExtentTestManager.getTest();

		Integer retryCount = (Integer) result.getAttribute("retryCount");
		Integer maxRetry = (Integer) result.getAttribute("maxRetry");

		// If retry is going to happen → mark as WARNING
		if (retryCount != null && retryCount < maxRetry) {

			test.warning("Test failed. Retrying " + retryCount + "/" + maxRetry);
			test.warning(result.getThrowable());

		} else {
			// Final failure → mark as FAIL
			test.fail(result.getThrowable());

			String path = ScreenshotUtil.captureScreenshot(result.getName());

			if (path != null && !path.isEmpty()) {
				try {
					test.addScreenCaptureFromPath(path);
				} catch (Exception e) {
					test.warning("Unable to attach screenshot");
				}
			}
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {

		ExtentTestManager.getTest().skip("Test Skipped: " + result.getName());
	}

	@Override
	public void onFinish(ITestContext context) {

		ExtentManager.getInstance().flush();
	}
	


}