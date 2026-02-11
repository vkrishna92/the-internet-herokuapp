package com.herokuapp.tests;

import com.herokuapp.helpers.ExtentReportHelper;
import com.herokuapp.helpers.VisualReasonEngine;
import com.herokuapp.models.AiResult;
import com.herokuapp.pageobjects.CheckBoxPage;
import com.herokuapp.pageobjects.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckBoxTests extends BaseTest {

    HomePage homePage;
    CheckBoxPage checkBoxPage;

    @Test(testName = "Validating Checkbox Clicks")
    public void checkBoxTest() {
        homePage = new HomePage(driver);
        homePage.clickOnMenuItem("Checkboxes");
        checkBoxPage = new CheckBoxPage(driver);
        //checkBoxPage.waitForPageToLoad();
        checkBoxPage.clickOnCheckBox1();
        checkBoxPage.clickOnCheckBox2();

        // Validating page elements alignment
        String prompt = "Validate if checkbox as aligned vertically to left.";
        AiResult aiResult = VisualReasonEngine.getInstance().validateUiImage(driver,prompt);
        ExtentReportHelper.getInstance().GetExtentTest().info("Reasoning: " + aiResult.getReason());
        Assert.assertTrue(aiResult.isResult());

        prompt = "Validate If Checkbox 1 is checked and Checkbox 2 is not checked.";
        aiResult = VisualReasonEngine.getInstance().validateUiImage(driver, prompt);
        ExtentReportHelper.getInstance().GetExtentTest().info("Reasoning: " + aiResult.getReason());
        Assert.assertTrue(aiResult.isResult());
    }
}
