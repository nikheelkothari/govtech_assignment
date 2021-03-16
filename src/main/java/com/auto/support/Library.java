package com.auto.support;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

/**
 * @author nikheelkumar
 * 
 * class holds operation related to {@link WebElement}, that is got form the {@link IDictionary}
 * 
 * few other features are 
 * returns the Web-Element from web page when IDictionary is passed as input
 * 
 */

public class Library implements IAbstractLibrary{
	
	/**
	 * @Description: the Web-Element from web page when IDictionary is passed as input
	 * @param an instance of Dictionary whose implementation provides selector or selector type
	 * @return Object of web Element from web page
	 */
	public WebElement getElement(IDictionary dict, WebDriver driver) {
		WebElement element = null;
		try {
			List<WebElement> elements = getElements(dict, driver);
			element = elements.size() > 0 ? elements.get(0) : null;
		} catch (Exception e) {
			e.getMessage();
		}
		return element;
	}

	/**
	 * @Description: list of the Web-Element from web page when IDictionary is passed as input
	 * @param an instance of Dictionary whose implementation provides selector or selector type
	 * @return list of web Element from web page
	 */
	public List<WebElement> getElements(IDictionary dict, WebDriver driver) {
		List<WebElement> elements = null;
		try {
			elements = tryAndFind(dict, driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elements;
	}

	public WebElement getSelector(IDictionary dict) {
		return null;
	}
	
	/**
	 * @Description: list of the Web-Element from web page when IDictionary is passed as input based on selector type
	 * @param an instance of Dictionary whose implementation provides selector or selector type
	 * @return list of web Element from web page
	 */
	public List<WebElement> tryAndFind(IDictionary dict, WebDriver driver){
		List<WebElement> elements = null;
		if(dict.getSelectorTypeDict() instanceof CssSelector) {
			elements = getElementByCSSSelector(dict.getSelectorDict(), driver);
		}else if(dict.getSelectorTypeDict() instanceof XPathSelector) {
			elements = getElementByXPathSelector(dict.getSelectorDict(), driver);
		}
		return elements;
	}
	
	/**
	 * @Description: list of the Web-Element from web page when IDictionary is passed as input for CSS selector
	 * @param an instance of Dictionary whose implementation provides selector or selector type
	 * @return list of web Element from web page
	 */
	public List<WebElement> getElementByCSSSelector(String selector, WebDriver driver){
		List<WebElement> elements = Lists.newArrayList();
		try {
			elements = driver.findElements(By.cssSelector(selector));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elements;
	}
	
	/**
	 * @Description: list of the Web-Element from web page when IDictionary is passed as input for xpath selector
	 * @param an instance of Dictionary whose implementation provides selector or selector type
	 * @return list of web Element from web page
	 */
	public List<WebElement> getElementByXPathSelector(String selector, WebDriver driver){
		List<WebElement> elements = Lists.newArrayList();
		try {
			elements = driver.findElements(By.xpath(selector));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elements;
	}
}