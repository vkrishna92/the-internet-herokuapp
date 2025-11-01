package com.herokuapp;

import com.herokuapp.pageobjects.AddRemoveElementsPage;
import com.herokuapp.pageobjects.HomePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HerokuTests extends BaseTest{

    HomePage homePage;
    AddRemoveElementsPage addRemoveElementsPage;

    @Test
    public void addRemoveButton(){
        homePage = new HomePage(driver);
        homePage.clickOnMenuItem("Add/Remove Elements");
        addRemoveElementsPage = new AddRemoveElementsPage(driver);
        addRemoveElementsPage.verifyPage();
        addRemoveElementsPage.clickAddElementButton();
        addRemoveElementsPage.clickDeleteButton();
        Assert.assertTrue(addRemoveElementsPage.isDeleteButtonPresent(), "Delete button should be deleted.");
    }

    @Test
    public void test2(){
        System.out.println("Test 2 successfull.");
    }
}
