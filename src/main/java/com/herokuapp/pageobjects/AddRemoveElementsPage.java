package com.herokuapp.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddRemoveElementsPage extends BasePage{

    private final By title = By.tagName("h3");
    private final By addElementButton = By.xpath("//button[text()='Add Element']");
    private final By deleteButton = By.xpath("//button[text()='Delete']");
    private final WebDriver driver;
    public AddRemoveElementsPage(WebDriver _driver) {
        super(_driver);
        driver = _driver;
    }

    public void verifyPage() {
        waitForElementToBePresent(title);
        waitForElementToBePresent(addElementButton);
    }

    // click on add element button
    public void clickAddElementButton() {
        waitForElementToBeClickable(addElementButton);
        driver.findElement(addElementButton).click();
    }

    public void clickDeleteButton() {
        waitForElementToBeClickable(deleteButton);
        driver.findElement(deleteButton).click();
    }

    // is Delete button present
    public boolean isDeleteButtonPresent() {
        return driver.findElements(deleteButton).size() == 0;
    }

}
