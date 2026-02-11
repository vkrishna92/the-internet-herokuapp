package com.herokuapp.tests;

import com.herokuapp.helpers.ExtentReportHelper;
import com.herokuapp.helpers.VisualReasonEngine;
import com.herokuapp.models.AiResult;
import com.herokuapp.pageobjects.HomePage;
import com.herokuapp.pageobjects.LoginPage;
import com.herokuapp.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {

    HomePage homePage;
    LoginPage loginPage;

    @Test(testName = "Validating Successful Login")
    public void testSuccessfulLogin() {
        homePage = new HomePage(driver);
        homePage.clickOnMenuItem("Form Authentication");

        loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login("tomsmith", "SuperSecretPassword!");

        String prompt = "Validate that the login was successful and a success message is displayed with text 'You logged into a secure area!'";
        AiResult aiResult = VisualReasonEngine.getInstance().validateUiImage(driver, prompt);
        ExtentReportHelper.getInstance().GetExtentTest().info("Reasoning: " + aiResult.getReason());
        Assert.assertTrue(aiResult.isResult(), "Login should be successful with valid credentials");
    }

    @Test(testName = "Validating Failed Login with Invalid Credentials")
    public void testFailedLogin() {
        homePage = new HomePage(driver);
        homePage.clickOnMenuItem("Form Authentication");

        loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();
        loginPage.login("invaliduser", "invalidpassword");

        String prompt = "Validate that the login failed and an error message is displayed with text 'Your username is invalid!'";
        AiResult aiResult = VisualReasonEngine.getInstance().validateUiImage(driver, prompt);
        ExtentReportHelper.getInstance().GetExtentTest().info("Reasoning: " + aiResult.getReason());
        Assert.assertTrue(aiResult.isResult(), "Login should fail with invalid credentials");
    }

    @Test(testName = "Validating Login Form Elements")
    public void testLoginFormElements() {
        homePage = new HomePage(driver);
        homePage.clickOnMenuItem("Form Authentication");

        loginPage = new LoginPage(driver);
        loginPage.waitForPageToLoad();

        String prompt = "Validate that the login form contains a username field, a password field, and a login button that are visible and properly aligned.";
        AiResult aiResult = VisualReasonEngine.getInstance().validateUiImage(driver, prompt);
        ExtentReportHelper.getInstance().GetExtentTest().info("Reasoning: " + aiResult.getReason());
        Assert.assertTrue(aiResult.isResult(), "Login form elements should be visible and properly aligned");
    }

    @AfterMethod()
    public void navigateToHome() {
        driver.get(BASE_URL);
    }
}
