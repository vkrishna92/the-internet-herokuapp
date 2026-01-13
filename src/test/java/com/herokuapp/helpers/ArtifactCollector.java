package com.herokuapp.helpers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Optional;

public final class ArtifactCollector {

    private static ArtifactCollector instance;
    private ArtifactCollector() {

    }

    public static ArtifactCollector getInstance() {
        if (instance == null) {
            instance = new ArtifactCollector();
        }
        return instance;
    }

    public boolean captureScreenshotPng(WebDriver driver) {
        if(driver == null) {
            return false;
        }
        String pngBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

        if(pngBase64 == null || pngBase64.isBlank()) {
            return false;
        }

        try {
            String fileName = "screenshotBase64_" + getTimeStamp() + ".txt";
            Files.writeString(Path.of(fileName), pngBase64, StandardCharsets.UTF_8);
            System.out.println("Successfully Saved Screenshot with file name ");
            return  true;

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public  boolean captureDomOuterHtml(WebDriver driver) {
        if (driver == null) {
            System.out.println("Driver is not initialized.");
            return false;
        }

        try {
            String outerDomJs = "return document && document.documentElement ? document.documentElement.outerHTML : null;";
            Object result = ((JavascriptExecutor) driver).executeScript(outerDomJs);

            if (result == null) return  false;

            String outerHtml = String.valueOf(result);
            String fileName = "outerDomHtml" + getTimeStamp() + ".txt";
            Files.writeString(Path.of(fileName), outerHtml, StandardCharsets.UTF_8);
            return  true;

        } catch (Exception ex) {
            System.out.println("Error while getting outer html.");
            ex.printStackTrace();
            return false;
        }
    }

    public boolean saveCurrentUrl(WebDriver driver) {
        if (driver == null) {
            System.out.println("Unable to save current url. Driver is null");
            return false;
        }

        String currentUrl = driver.getCurrentUrl();

        try {
            String fileName = "currentHtml" + getTimeStamp() + ".txt";
            Files.writeString(Path.of(fileName), currentUrl, StandardCharsets.UTF_8);
            return true;
        }
        catch (Exception ex) {
            System.out.println("Error while saving currentUrl to file.");
            ex.printStackTrace();
            return false;
        }
    }

    private String getTimeStamp() {
        return DateTimeHelper.getInstance().getTimeStamp();
    }
}
