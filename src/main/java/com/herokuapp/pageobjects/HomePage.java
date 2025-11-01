package com.herokuapp.pageobjects;

import com.herokuapp.utils.LoggerHelper;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage{

    private final WebDriver driver;
    private final Logger log = LoggerHelper.getLogger(HomePage.class);

    public HomePage(WebDriver _driver) {
        super(_driver);
        driver = _driver;
    }

    public void clickOnMenuItem(String menuItem) {
        log.info("Clicking on menu item: " + menuItem);
        driver.findElement(By.partialLinkText(menuItem)).click();
    }

}
