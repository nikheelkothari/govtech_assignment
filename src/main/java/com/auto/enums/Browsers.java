package com.auto.enums;

public enum Browsers {

    CHROME("chrome");

    private final String browsers;

    Browsers(final String browser) {
        this.browsers = browser;
    }

    public String browsers() {
        return browsers;
    }

    public static Browsers value(final String browser) {
        try {
            return Browsers.valueOf(browser.toUpperCase());
        }
        catch (final IllegalArgumentException e) {
            return CHROME;
        }
    }
}
