package com.herokuapp.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class DropdownPage extends BasePage{

    private final By dropdown = By.id("dropdown");
    private final WebDriver driver;
    public DropdownPage(WebDriver _driver) {
        super(_driver);
        driver = _driver;
    }

    public void verifyPage() {
        waitForElementToBePresent(dropdown);
    }

    public  void selectOption(String option) {
        Select select = new Select(driver.findElement(dropdown));
        select.selectByVisibleText(option);
    }

    public String getSelectedOption() {
        Select select = new Select(driver.findElement(dropdown));
        return select.getFirstSelectedOption().getText();
    }
}
