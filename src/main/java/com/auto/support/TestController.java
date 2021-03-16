package com.auto.support;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import com.auto.utils.ActionUtils;

/**
 * @author nikheelkumar
 */

public abstract class TestController extends AbstractTestNGSpringContextTests  {

	public ActionUtils actionUtils = new ActionUtils();
	public Library library = new Library();
	protected abstract void setApplicationUrl(String appUrl);
}
