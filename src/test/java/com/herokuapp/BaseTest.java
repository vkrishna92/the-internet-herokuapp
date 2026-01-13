package com.herokuapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.herokuapp.helpers.WebDriverManager;
import okhttp3.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.devicefarm.DeviceFarmClient;
import software.amazon.awssdk.services.devicefarm.model.CreateTestGridUrlRequest;
import software.amazon.awssdk.services.devicefarm.model.CreateTestGridUrlResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    private final static String AWS_DEVICE_FARM = "aws-device-farm";
    protected WebDriver driver;
    @BeforeSuite
    public void beforeSuite() {
        System.out.println("Before Suite");
    }

    @BeforeClass
    @Parameters({"platform"})
    public  void beforeClass(String platformType) {
        System.out.println("Before Class");
        System.out.println("Platform: " + platformType);
        try {
            System.out.println("Initializing driver.");
            if (platformType.equals(AWS_DEVICE_FARM)) {
                initiateDeviceFarmBrowser();
            }
            else {
                initiateDriver();

            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("Before Test");
        initializeTestRun("","");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("After Test");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("After Class");
        closeDriver();
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("After Suite");
    }

    private void initiateDriver() {
        ChromeOptions options = getChromeOptions(true);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://the-internet.herokuapp.com/");
        try {
            WebDriverManager.getInstance().setDriver(driver);
        } catch (Exception e) {
            System.out.println("Error while setting driver in DriverManager");
        }
    }

    private void closeDriver() {
        driver.quit();
    }

    private void initiateDeviceFarmBrowser() throws MalformedURLException {
        String projectARN = "arn:aws:devicefarm:us-west-2:140023384358:testgrid-project:38759bc7-4382-461e-9fa2-e1b1e9e5eb23";
        DeviceFarmClient client = DeviceFarmClient.builder().region(Region.US_WEST_2).build();
        CreateTestGridUrlRequest request = CreateTestGridUrlRequest.builder()
                .expiresInSeconds(300)
                .projectArn(projectARN)
                .build();

        CreateTestGridUrlResponse respone = client.createTestGridUrl(request);
        System.out.println(respone.toString());
        URL testGridUrl = new URL(respone.url());

        ChromeOptions options = getChromeOptions(false);
        driver = new RemoteWebDriver(testGridUrl, options);
        String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
        System.out.println("Session ID: " + sessionId);
        driver.manage().window().maximize();
        driver.get("https://the-internet.herokuapp.com/");
        try {
            WebDriverManager.getInstance().setDriver(driver);
        } catch (Exception e) {
            System.out.println("Error while setting driver in DriverManager");
        }
    }

    private ChromeOptions getChromeOptions(boolean isHeadless) {
        ChromeOptions options = new ChromeOptions();
        if (isHeadless) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private void initializeTestRun(String testRunName, String testRunNumber) {
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(Duration.ofSeconds(30))
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(30))
                .build();
        String jsonPayload = """
                {
                  "testRunId": 0,
                  "runName": "Heroku Test Runs",
                  "runNumber": "string",
                  "status": "Running",
                  "startTime": "2025-12-25T06:34:00.152Z",
                  "endTime": "2025-12-25T06:34:00.152Z",
                  "durationMs": 0,
                  "triggeredBy": "string",
                  "triggerType": "local",
                  "ciProvider": "",
                  "ciBuildUrl": "",
                  "sourceBranch": "",
                  "commitSha": "",
                  "totalCount": 0,
                  "passedCount": 0,
                  "failedCount": 0,
                  "skippedCount": 0,
                  "metadataJson": ""
                }
        """;
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(jsonPayload);
            ObjectNode rootObject = (ObjectNode) root;
            rootObject.put("runName", testRunName);
            RequestBody requestBody = RequestBody.create(rootObject.toString(), MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url("http://localhost:5093/api/TestRun")
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
