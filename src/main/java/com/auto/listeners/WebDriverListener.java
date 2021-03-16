package com.auto.listeners;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import com.auto.config.BrowserConfig;
import com.auto.driver.WebDriverFactory;
import com.auto.enums.Browsers;


public class WebDriverListener implements IInvokedMethodListener {

	private static final ThreadLocal<Boolean> IS_DRIVER_CREATED = new ThreadLocal<Boolean>();
	private static String browser;

	private static String getBrowser(final Map<String, String> allParams) {
		final String envVal = System.getenv("browser");
		final String propVal = System.getProperty("browser");

		final String browser = StringUtils.firstNonEmpty(envVal, propVal, allParams.get("browser"));
		return Browsers.value(browser).browsers();
	}

	public void beforeInvocation(final IInvokedMethod method, final ITestResult result) {

		if(IS_DRIVER_CREATED.get() == null) {
			IS_DRIVER_CREATED.set(false);
		}

		if((method.isTestMethod() || method.isConfigurationMethod()) && !IS_DRIVER_CREATED.get()) {
			/*
              if browser name passed as command line argument, then automation will create that browser instance
              if no command line argument, then it will check whether browser name provided in testng xml
              if no, then chrome driver object will be created.
			 */

			final BrowserConfig browserConfig = new BrowserConfig();
			final Map<String, String> allParams = result.getTestContext().getCurrentXmlTest().getAllParameters();

			//this will set the browser if the test is for desktop
			// this value needs to read only once for the entire suite.
			if(browser == null) {
				browser = getBrowser(allParams);
			}
			browserConfig.setBrowserName(browser);

			browserConfig.setTestName(method.getTestMethod().getMethodName());

			String browserMode = allParams.get("browserMode");
			if (!StringUtils.isEmpty(browserMode) && "headless".equalsIgnoreCase(browserMode)) {
				browserConfig.isHeadless = true;
			}

			WebDriverFactory.setDriver(browserConfig);
			IS_DRIVER_CREATED.set(true); //Driver created for the current testcase.
		}
	}

	public void afterInvocation(final IInvokedMethod method, final ITestResult testResult) {
		
		IS_DRIVER_CREATED.set(false);
		
//		if(method.isTestMethod()) {
//			IS_DRIVER_CREATED.set(false);
//			//val driver = WebDriverFactory.getDriver();
//
//			WebDriver driver = null;
//			Object testObject = testResult.getInstance();
//			Class<?> testClass = testResult.getTestClass().getRealClass();
//			try {
//				Field field = testClass.getDeclaredField("driver");
//				driver = (WebDriver) field.get(testObject);
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//
//			if (!testResult.isSuccess() && (driver != null)) {
//				//ReportingUtil.addToTestNGReport(driver, testResult);
//				//ReportingUtil.addToAllureReport(driver, testResult);
//				//FlexoHelper.sendMetadataToFlexo(driver, method, testResult);
//			}
//
//			if (driver != null) {
//				System.out.println("afterInvocation: " + Thread.currentThread().getId());
//				driver.close();
//				driver.quit();
//			}
//		}
	}
}
