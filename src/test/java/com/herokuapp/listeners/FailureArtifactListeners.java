package com.herokuapp.listeners;

import com.herokuapp.helpers.ArtifactCollector;
import com.herokuapp.helpers.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class FailureArtifactListeners implements ITestListener {


    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Method Failed" + result.getName());
        try {
            WebDriver driver = WebDriverManager.getInstance().getDriver();
            ArtifactCollector artifactCollector = ArtifactCollector.getInstance();
            artifactCollector.saveCurrentUrl(driver);
            artifactCollector.captureDomOuterHtml(driver);
            artifactCollector.captureScreenshotPng(driver);
        }
        catch (Exception ex) {
            System.out.println("Error while saving artifacts.");
            ex.printStackTrace();
        }
    }
}
