package com.auto.support;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Description: IAbstractLibrary interface consist of method declaration
 * @author nikheelkumar
 */

public interface IAbstractLibrary {
	
	/**
	 * @Description: the Web-Element from web page when IDictionary is passed as input
	 * @param an instance of Dictionary whose implementation provides selector or selector type
	 * @return Object of web Element from web page
	 */
	public WebElement getElement(IDictionary dict, WebDriver driver);
	
	/**
	 * @Description: gets the list of Web-Element from web page when IDictionary is passed as input
	 * @param an instance of Dictionary whose implementation provides selector or selector type
	 * @return Object of web Element from web page
	 */
	public List<WebElement> getElements(IDictionary dict, WebDriver driver);
	
	/**
	 * @Description: gets the selector in string format
	 * @param an instance of Dictionary whose implementation provides selector or selector type
	 * @return Object of web Element from web page
	 */
	public WebElement getSelector(IDictionary dict);
	
}
