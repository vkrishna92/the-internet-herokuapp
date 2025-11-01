package com.herokuapp;

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
        homePage.clickOnMenuItem("Add/Remove Elements");
        addRemoveElementsPage = new AddRemoveElementsPage(driver);
        addRemoveElementsPage.verifyPage();
        addRemoveElementsPage.clickAddElementButton();
        addRemoveElementsPage.clickDeleteButton();
        Assert.assertTrue(addRemoveElementsPage.isDeleteButtonPresent(), "Delete button should be deleted.");
    }

    @Test
    public void dropdownTest(){
        homePage = new HomePage(driver);
        homePage.clickOnMenuItem("Dropdown");
        dropdownPage = new DropdownPage(driver);
        dropdownPage.verifyPage();
        dropdownPage.selectOption("Option 2");
        Assert.assertEquals(dropdownPage.getSelectedOption(), "Option 2", "Option 2 should be selected.");
    }
}
