package com.herokuapp.tests;

import com.herokuapp.helpers.ExtentReportHelper;
import com.herokuapp.helpers.VisualReasonEngine;
import com.herokuapp.models.AiResult;
import com.herokuapp.pageobjects.AddRemoveElementsPage;
import com.herokuapp.pageobjects.DropdownPage;
import com.herokuapp.pageobjects.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HerokuTests extends BaseTest {

    HomePage homePage;
    AddRemoveElementsPage addRemoveElementsPage;
    DropdownPage dropdownPage;

    @Test(testName = "Validating Button Clicks")
    public void addRemoveButtonTest(){
        homePage = new HomePage(driver);
        String prompt = "";
        homePage.clickOnMenuItem("Add/Remove Elements");
        addRemoveElementsPage = new AddRemoveElementsPage(driver);
        addRemoveElementsPage.verifyPage();
        addRemoveElementsPage.clickAddElementButton();
        // AI Validation
        prompt = "Delete button is displayed and Add Element is displayed.";
        AiResult aiResult = VisualReasonEngine.getInstance().validateUiImage(driver, prompt);
        ExtentReportHelper.getInstance().GetExtentTest().info("Reasoning: " + aiResult.getReason());
        Assert.assertTrue(aiResult.isResult());

        // Click delete button
        addRemoveElementsPage.clickDeleteButton();

        // AI Validation
        prompt = "Delete button is not displayed and Add Element is displayed";
        aiResult = VisualReasonEngine.getInstance().validateUiImage(driver, prompt);
        ExtentReportHelper.getInstance().GetExtentTest().info("Reasoning: " + aiResult.getReason());
        Assert.assertTrue(aiResult.isResult());
    }
}
