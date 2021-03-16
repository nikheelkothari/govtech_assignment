package com.auto.config;


public class BrowserConfig {
    public String browserName = "CHROME";
    public boolean isHeadless = false;
    public String testName;
	
    public String getBrowserName() {
		return browserName;
	}
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
	public boolean isHeadless() {
		return isHeadless;
	}
	public void setHeadless(boolean isHeadless) {
		this.isHeadless = isHeadless;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
}
