package com.herokuapp.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private WebDriver _driver;

    private final By usernameFieldLocator = By.xpath("//input[@id='username']");
    private final By passwordFieldLocator = By.xpath("//input[@id='password']");
    private final By loginButtonLocator = By.xpath("//button[@type='submit']");
    private final By flashBannerLocator = By.xpath("//div[@id='flash-messages']");
    private final By flashMessageLocator = By.xpath("//div[@id='flash-messages']/div");

    public LoginPage(WebDriver driver) {
        super(driver);
        _driver = driver;
    }

    public void enterUsername(String username) {
        waitForElementToBePresent(usernameFieldLocator);
        _driver.findElement(usernameFieldLocator).clear();
        _driver.findElement(usernameFieldLocator).sendKeys(username);
    }

    public void enterPassword(String password) {
        waitForElementToBePresent(passwordFieldLocator);
        _driver.findElement(passwordFieldLocator).clear();
        _driver.findElement(passwordFieldLocator).sendKeys(password);
    }

    public void clickLoginButton() {
        waitForElementToBeClickable(loginButtonLocator);
        _driver.findElement(loginButtonLocator).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    @Override
    public void waitForPageToLoad() {
        waitForElementToBePresent(usernameFieldLocator);
        waitForElementToBePresent(passwordFieldLocator);
        waitForElementToBePresent(loginButtonLocator);
    }

    public void waitForFlashMessageToBeVisible(){
        waitForElementToBeVisisble(flashBannerLocator);
    }

    public String getFlashMessage() {
        waitForElementToBePresent(flashMessageLocator);
        return _driver.findElement(flashMessageLocator).getText();
    }
}
