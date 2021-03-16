package com.auto.listeners;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.auto.reporting.ExtentManager;
import com.auto.support.TestDetails;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

/**
 * 
 * @author nikheelkumar
 *	This is the main listener class
 *	List of few functionality: Extent Reporting implementation, Capture Screens
 */

public class TestListener implements ITestListener, ISuiteListener {

	// Extent Report Declarations
	private static ExtentReports extent = ExtentManager.createInstance();
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	private String testCaseDescription;

	@SuppressWarnings("deprecation")
	public void onTestStart(ITestResult result) {
		testCaseDescription = result.getMethod().getMethod().getAnnotation(TestDetails.class).description();
		String suiteName = result.getTestContext().getSuite().getName();
		String groupName = result.getTestContext().getName();
		ExtentTest extentTest = extent.createTest(testCaseDescription, suiteName + " - " + groupName);
		extentTest.assignCategory(groupName);
		test.set(extentTest);
	}

	@SuppressWarnings("deprecation")
	public void onTestSuccess(ITestResult testResult) {
		testCaseDescription = testResult.getMethod().getMethod().getAnnotation(TestDetails.class).description();
		try {
			printTestResults(testResult, testCaseDescription);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void onTestFailure(ITestResult testResult) {
		testCaseDescription = testResult.getMethod().getMethod().getAnnotation(TestDetails.class).description();
		try {
			printTestResults(testResult, testCaseDescription);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void onTestSkipped(ITestResult testResult) {
		testCaseDescription = testResult.getMethod().getMethod().getAnnotation(TestDetails.class).description();
		try {
			printTestResults(testResult, testCaseDescription);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onFinish(ISuite suite) {
		extent.flush();
	}
	
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	public void onStart(ITestContext context) {
	}

	public void onFinish(ITestContext context) {
	}

	public void onStart(ISuite suite) {
	}

	private void printTestResults(ITestResult testResult, String testCaseName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		
		WebDriver driver = null;
		Object testObject = testResult.getInstance();
		Class<?> testClass = testResult.getTestClass().getRealClass();
		try {
			Field field = testClass.getDeclaredField("driver");
			driver = (WebDriver) field.get(testObject);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String screenShotPath = capture(driver, dateName);
		test.get().assignAuthor("Nikheelkumar");
		
		switch (testResult.getStatus()) {
		case ITestResult.SUCCESS:
			test.get().pass("Test passed");
			if(screenShotPath != null) {
				test.get().addScreenCaptureFromPath(screenShotPath);
			}
			test.get().log(Status.PASS, testCaseName);
			break;
		case ITestResult.FAILURE:
			//test.get().fail(result.getThrowable());
			test.get().fail(testResult.getThrowable().getLocalizedMessage());
			if(screenShotPath != null) {
				test.get().addScreenCaptureFromPath(screenShotPath);
			}
			test.get().log(Status.FAIL, testCaseName);
			break;
		case ITestResult.SKIP:
			test.get().skip(testResult.getThrowable());
			if(screenShotPath != null) {
				test.get().addScreenCaptureFromPath(screenShotPath);
			}
			test.get().log(Status.SKIP, "Snapshot below: " + test.get().addScreenCaptureFromPath(screenShotPath));
			break;
		}
	}

	public static String capture(WebDriver driver, String screenShotName) throws IOException {
		String dest = null;
		if(driver != null) {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			dest = System.getProperty("user.dir") + "\\test-report\\" + screenShotName + ".png";
			File destination = new File(dest);
			FileUtils.copyFile(source, destination);
		}
		return dest;
	}
}
