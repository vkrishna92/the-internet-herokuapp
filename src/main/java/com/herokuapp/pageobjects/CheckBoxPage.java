package com.herokuapp.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckBoxPage extends BasePage{
    private WebDriver _driver;

    private final By checkBox1Locator = By.xpath("//form[@id='checkboxes']/input[1]");
    private final By checkBox2Locator = By.xpath("//form[@id='checkboxes']/input[2]");

    public CheckBoxPage(WebDriver driver) {
        super(driver);
        _driver = driver;
    }

    public void clickOnCheckBox1() {
        waitForElementToBeClickable(checkBox1Locator);
        _driver.findElement(checkBox1Locator).click();
    }

    public void clickOnCheckBox2() {
        waitForElementToBeClickable(checkBox2Locator);
        _driver.findElement(checkBox2Locator).click();
    }

    @Override
    public void waitForPageToLoad() {
        waitForElementToBePresent(checkBox1Locator);
        waitForElementToBePresent(checkBox2Locator);
    }
}
