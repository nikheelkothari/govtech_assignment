package com.auto.support;

/**
 * @author nikheelkumar
 */

public class XPathSelector implements ISelector {

	// the selector
	public String selector;
	
	/**
	 * initiates a new xpath selector.
	 * @param selector - is in string format
	 */
	public XPathSelector(String selector) {
		this.selector = selector ;
	}
	
	/**
	 * it returns String selector type.
	 */
	public String getSelector() {
		return this.selector;
	}
}
