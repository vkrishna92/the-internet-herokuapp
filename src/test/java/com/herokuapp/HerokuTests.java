package com.herokuapp;

import com.herokuapp.helpers.VisualReasonEngine;
import com.herokuapp.pageobjects.AddRemoveElementsPage;
import com.herokuapp.pageobjects.DropdownPage;
import com.herokuapp.pageobjects.HomePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HerokuTests extends BaseTest{

    HomePage homePage;
    AddRemoveElementsPage addRemoveElementsPage;
    DropdownPage dropdownPage;

    @Test
    public void addRemoveButtonTest(){
        homePage = new HomePage(driver);
        String prompt = "";
        homePage.clickOnMenuItem("Add/Remove Elements");
        addRemoveElementsPage = new AddRemoveElementsPage(driver);
        addRemoveElementsPage.verifyPage();
        addRemoveElementsPage.clickAddElementButton();
        prompt = "Delete button is displayed and Add Element is displayed.";
        Assert.assertTrue(VisualReasonEngine.getInstance().validateUiImage(driver,prompt));
        addRemoveElementsPage.clickDeleteButton();
        prompt = "Delete button is not displayed and Add Element is displayed";
        Assert.assertTrue(VisualReasonEngine.getInstance().validateUiImage(driver, prompt));
    }
}
