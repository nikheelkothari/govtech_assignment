package com.auto.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.auto.commons.IConstants;
import com.auto.support.CssSelector;
import com.auto.support.IDictionary;
import com.auto.support.Library;

public class ActionUtils {

	private Library library = new Library();

	public boolean waitForPageLoadingJs(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		// Wait for java Script to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};

		driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
		boolean bool = (wait.until(jsLoad));
		return bool;
	}
	
	public void scrollByVisibleElement(IDictionary dict, WebDriver driver) {
		WebElement element = library.getElement(dict, driver);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		// This will scroll the page till the element is found
		executor.executeScript("arguments[0].scrollIntoView(true);",element);
	}
	
	public void uploadFile(IDictionary dict, WebDriver driver, String filePath) {
		WebElement element = library.getElement(dict, driver);
		element.sendKeys(filePath);
	}
	
	public int getListSize(IDictionary dict, WebDriver driver) {
		List<WebElement> elements = library.getElements(dict, driver);
		return elements.size();
	}
	 
	public WebElement waitForElementVisibility(IDictionary dict, Integer timeout, WebDriver driver) {
		WebElement element = null;
		if (dict.getSelectorTypeDict() instanceof CssSelector) {
			element = new WebDriverWait(driver, timeout.longValue())
					.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(dict.getSelectorDict())));
		} else {
			element = new WebDriverWait(driver, timeout.longValue())
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dict.getSelectorDict())));
		}
		return element;
	}

	public WebElement waitForElementToBeClickable(IDictionary dict, Integer timeout, WebDriver driver) {
		WebElement element = null;
		if (dict.getSelectorTypeDict() instanceof CssSelector) {
			element = new WebDriverWait(driver, timeout.longValue())
					.until(ExpectedConditions.elementToBeClickable(By.cssSelector(dict.getSelectorDict())));
		} else {
			element = new WebDriverWait(driver, timeout.longValue())
					.until(ExpectedConditions.elementToBeClickable(By.xpath(dict.getSelectorDict())));
		}
		return element;
	}

	public void typeIn(IDictionary dict, String characters, WebDriver driver) {
		try {
			WebElement element = waitForElementVisibility(dict, IConstants.DEFAULT_WAIT, driver);
			if (element != null) {
				element.clear();
				element.sendKeys(characters);
			} else {
				Reporter.log("Input Element is Null", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void click(IDictionary dict, WebDriver driver) {
		try {
			WebElement element = waitForElementToBeClickable(dict, IConstants.DEFAULT_WAIT, driver);
			if (element.isEnabled()) {
				element.click();
			} else {
				Reporter.log("Click Element is Null", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickJs(IDictionary dict, WebDriver driver) {
		try {
			WebElement element = waitForElementToBeClickable(dict, IConstants.DEFAULT_WAIT, driver);
			if (element.isEnabled()) {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean getFrameById(final String id, WebDriver driver) {
		final List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
		for (WebElement iframe : iframes) {
			if (iframe.getAttribute("id").equals(id)) {
				return true;
			}
		}
		return false;
	}

	public boolean getFrameByName(final String name, WebDriver driver) {
		final List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
		for (WebElement iframe : iframes) {
			if (iframe.getAttribute("name").equals(name)) {
				return true;
			}
		}
		return false;
	}

	public void verifyTest(boolean fail, String issue) {
		Assert.assertTrue(fail, issue);
	}

	public String getInputValue(IDictionary dict, WebDriver driver) {
		String textVlaue = null;
		try {
			textVlaue = library.getElement(dict, driver).getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textVlaue;
	}
}
