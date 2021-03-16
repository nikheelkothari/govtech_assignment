package com.auto.support;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.auto.driver.WebDriverFactory;

/**
 * @author nikheelkumar
 */

@ContextConfiguration(locations = "classpath:context.xml")
public class BaseController extends TestController {

	private String applicationUrl;
	protected WebDriver driver = null;
	
	public WebDriver getDriver() {
		return driver;
	}

	@Override
	@Autowired
	@Qualifier("applicationUrl")
	protected void setApplicationUrl(String appUrl) {
		applicationUrl = appUrl;
	}

	private void openApp() {
		driver = WebDriverFactory.getDriver();
		driver.get(applicationUrl);
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeEachTestSetup() {
		openApp();
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		if(driver != null) {
			driver.close();
			driver.quit();
		}
	}
	
	protected boolean reportTestIssues(ArrayList<String> issuelist) {
		boolean passFail = true;
		//List<String> issueListDistinct = issuelist.stream().distinct().collect(Collectors.toList());
		if(issuelist.size() > 0) {
			StringBuilder issueBuilder = new StringBuilder();
			for(int i = 0 ; i < issuelist.size(); i ++) {
				issueBuilder.append("" + ( i + 1) + "- " + issuelist.get(i));
				issueBuilder.append("<br />");
				issueBuilder.append("<br />");
			}
			passFail = false; 	
			actionUtils.verifyTest(false, issueBuilder.toString().replaceAll(", $", ""));
		}
		return passFail;
	}
}
