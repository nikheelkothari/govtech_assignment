package com.auto.driver;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.auto.config.BrowserConfig;
import com.auto.config.ChromeBrowserConfig;
import com.auto.enums.Browsers;

public class WebDriverFactory {

    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

    public synchronized static void setDriver (final BrowserConfig config) {
    	if (config.getBrowserName().toUpperCase().equals(Browsers.CHROME.toString())) {
            tlDriver = ThreadLocal.withInitial(new Supplier<WebDriver>() {
				public WebDriver get() {
					WebDriver driver = new ChromeDriver(ChromeBrowserConfig.getBaseChromeOptions(config));
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					return driver;
				}
			});
        } 
    }

    public synchronized static WebDriver getDriver () {
        return tlDriver.get();
    }

}
