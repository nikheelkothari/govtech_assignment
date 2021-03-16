package com.auto.config;

import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.HashMap;
import java.util.Map;

public class ChromeBrowserConfig {
     public static ChromeOptions getBaseChromeOptions(final BrowserConfig config) {
    	WebDriverManager.chromedriver().setup();
        final Map<String, Object> chromePreferences = new HashMap<String, Object>();
        chromePreferences.put("profile.password_manager_enabled", false);

        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", chromePreferences);

        // Needed to run the tests in docker
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("test-type");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.setExperimentalOption("w3c", false);

        if(config.isHeadless) {
            chromeOptions.addArguments("--headless");
        }

        return chromeOptions;
    }
}
