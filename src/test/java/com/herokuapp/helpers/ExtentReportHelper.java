package com.herokuapp.helpers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportHelper {
    private static ExtentReportHelper INSTANCE;
    private ExtentReports extentReports;
    protected ExtentSparkReporter spark;
    private ExtentTest extentTest;
    private ExtentReportHelper() {
    }

    public static ExtentReportHelper getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ExtentReportHelper();
        }
        return INSTANCE;
    }

    public void CreateExtentReport() {
        extentReports = new ExtentReports();
        spark = new ExtentSparkReporter("resources/Spark/Spark.html");
        extentReports.attachReporter(spark);
    }

    public void CreateTest(String testName) {
        extentTest = extentReports.createTest(testName);
    }

    public ExtentTest GetExtentTest() {
        return  extentTest;
    }

    public void FlushReport() {
        extentReports.flush();
    }



}
