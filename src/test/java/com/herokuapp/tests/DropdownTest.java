package com.herokuapp.tests;

import com.herokuapp.helpers.ExtentReportHelper;
import com.herokuapp.helpers.VisualReasonEngine;
import com.herokuapp.models.AiResult;
import com.herokuapp.pageobjects.AddRemoveElementsPage;
import com.herokuapp.pageobjects.DropdownPage;
import com.herokuapp.pageobjects.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DropdownTest extends BaseTest {

    HomePage homePage;
    AddRemoveElementsPage addRemoveElementsPage;
    DropdownPage dropdownPage;

    @Test(testName = "Validating Dropdown Select")
    public void dropdownTest(){
        homePage = new HomePage(driver);
        homePage.clickOnMenuItem("Dropdown");
        dropdownPage = new DropdownPage(driver);
        dropdownPage.verifyPage();
        dropdownPage.selectOption("Option 2");
        String prompt = "Option 2 is selected on dropdown. There is only one Dropdown List.";
        AiResult aiResult = VisualReasonEngine.getInstance().validateUiImage(driver, prompt);
        ExtentReportHelper.getInstance().GetExtentTest().info("Reasoning: " + aiResult.getReason());
        Assert.assertTrue(aiResult.isResult());
    }
}
