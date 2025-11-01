package com.herokuapp;

import com.herokuapp.pageobjects.AddRemoveElementsPage;
import com.herokuapp.pageobjects.DropdownPage;
import com.herokuapp.pageobjects.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DropdownTest extends BaseTest{

    HomePage homePage;
    AddRemoveElementsPage addRemoveElementsPage;
    DropdownPage dropdownPage;

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
