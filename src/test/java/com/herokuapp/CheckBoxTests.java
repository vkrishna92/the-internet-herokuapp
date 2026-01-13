package com.herokuapp;

import com.herokuapp.helpers.VisualReasonEngine;
import com.herokuapp.pageobjects.CheckBoxPage;
import com.herokuapp.pageobjects.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckBoxTests extends BaseTest{

    HomePage homePage;
    CheckBoxPage checkBoxPage;

    @Test
    public void checkBoxTest() {
        homePage = new HomePage(driver);
        homePage.clickOnMenuItem("Checkboxes");
        checkBoxPage = new CheckBoxPage(driver);
        //checkBoxPage.waitForPageToLoad();
        checkBoxPage.clickOnCheckBox1();
        checkBoxPage.clickOnCheckBox2();

        String prompt = "Validate If Checkbox 1 is checked and Checkbox 2 is unchecked.";
        Assert.assertTrue(VisualReasonEngine.getInstance().validateUiImage(driver, prompt));

    }
}
