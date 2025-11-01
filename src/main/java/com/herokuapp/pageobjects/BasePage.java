package com.herokuapp.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    protected void  waitForElementToBePresent(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected void waitForElementToBeClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForTextToBePresent(By locator, String text) {
        wait.until(ExpectedConditions.textToBe(locator, text));
    }
}
