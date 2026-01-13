package com.herokuapp.helpers;

import org.openqa.selenium.WebDriver;

public final class WebDriverManager {

    private static WebDriverManager driverManager;
    private WebDriver driver;

    private WebDriverManager() {

    }

    public static final WebDriverManager getInstance() throws Exception {
        if (driverManager == null) {
            driverManager = new WebDriverManager();
        }
        return driverManager;
    }

    public void setDriver(WebDriver _driver) {
        driver = _driver;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
