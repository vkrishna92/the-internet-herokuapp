package com.herokuapp.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.herokuapp.helpers.ArtifactCollector;
import com.herokuapp.helpers.ExtentReportHelper;
import com.herokuapp.helpers.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class FailureArtifactListeners implements ITestListener, ISuiteListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportHelper.getInstance().CreateTest(result.getName());
    }

    @Override
    public void onStart(ISuite suite) {
        ExtentReportHelper.getInstance().CreateExtentReport();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentReportHelper.getInstance().FlushReport();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            ExtentReportHelper.getInstance()
                    .GetExtentTest()
                    .addScreenCaptureFromBase64String(WebDriverManager.getInstance().getScreenShotBase64());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ExtentReportHelper.getInstance().GetExtentTest().fail("fail");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            ExtentReportHelper.getInstance()
                    .GetExtentTest()
                    .addScreenCaptureFromBase64String(WebDriverManager.getInstance().getScreenShotBase64());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ExtentReportHelper.getInstance().GetExtentTest().pass("pass");
    }
}
