package com.auto.support;

/**
 * @author nikheelkumar
 */

public class CssSelector implements ISelector{
	
	// the selector
	public String selector;
	
	/**
	 * initiates a new css selector.
	 * @param selector - is in string format
	 */
	public CssSelector(String selector) {
		this.selector = selector ;
	}
	
	/**
	 * it returns String selector type.
	 */
	public String getSelector() {
		return this.selector;
	}
}
