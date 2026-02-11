# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Selenium WebDriver test automation framework for [The Internet](https://the-internet.herokuapp.com/) that combines traditional UI testing with AI-powered visual validation using AWS Bedrock (Claude Sonnet 4.5).

## Build & Test Commands

### Local Execution
```bash
# Run all tests (default suite)
mvn clean test -DsuiteXmlFile=src/test/resources/testng.xml

# Run AWS Device Farm tests
mvn clean test -DsuiteXmlFile=src/test/resources/testng-aws-devicefarm.xml

# Run specific test class
mvn test -Dtest=CheckBoxTests

# Run specific test method
mvn test -Dtest=CheckBoxTests#testCheckboxAlignment
```

### Docker Execution
```bash
docker build -t selenium-tests .
docker run selenium-tests
```

### Build Only (Skip Tests)
```bash
mvn clean package -DskipTests
```

## Architecture Overview

### Core Design Pattern: Page Object Model (POM)

**Page Objects** (`src/main/java/com/herokuapp/pageobjects/`)
- `BasePage`: Parent class providing common WebDriverWait utilities and wait methods
- Each page class (HomePage, CheckBoxPage, DropdownPage, AddRemoveElementsPage) encapsulates page-specific elements and interactions
- Page objects receive WebDriver via constructor injection

**Tests** (`src/test/java/com/herokuapp/`)
- All test classes extend `BaseTest`
- BaseTest manages WebDriver lifecycle via TestNG annotations (@BeforeSuite, @BeforeClass, @AfterClass, etc.)
- Tests use Page Objects for interactions and AI validation for assertions

### Singleton Helper Classes

All helpers follow the Singleton pattern and are located in `src/test/java/com/herokuapp/helpers/`:

1. **WebDriverManager** - Thread-safe WebDriver instance management, provides screenshot capture
2. **VisualReasonEngine** - AI-powered UI validation using AWS Bedrock (see below)
3. **ExtentReportHelper** - Manages HTML test reports (output: `resources/Spark/Spark.html`)
4. **BedrockClientHelper** - AWS Bedrock client for Claude Sonnet 4.5 (`us.anthropic.claude-sonnet-4-5-20250929-v1:0`)
5. **ArtifactCollector** - Captures screenshots, DOM HTML, and URLs with timestamps
6. **DateTimeHelper** - Consistent timestamp formatting (`yyyyMMdd_HHmmss_SSS`)

### AI-Powered Visual Validation

The framework's unique feature is **natural language UI assertions** using Claude Sonnet 4.5:

```java
// Traditional assertion approach
Assert.assertTrue(element.isDisplayed(), "Element should be visible");

// AI validation approach (used in this framework)
AiResult result = VisualReasonEngine.getInstance()
    .validateUiImage(driver, "The delete button should be present and visible");
Assert.assertTrue(result.result(), result.reason());
```

**How it works:**
1. `VisualReasonEngine` captures a screenshot as Base64 PNG
2. Sends screenshot + natural language prompt to Claude Sonnet 4.5 via AWS Bedrock Converse API
3. LLM analyzes the image and returns JSON: `{"result": true/false, "reason": "explanation"}`
4. Result is parsed into `AiResult` model and used in assertions

**Benefits:**
- More resilient to minor UI changes (no brittle XPath/CSS locators in assertions)
- Natural language requirements map directly to validation prompts
- Detailed reasoning for failures aids debugging

## Platform Configuration

Tests support two execution platforms via TestNG parameter:

### Local Platform (`platform="local"`)
- Uses local ChromeDriver in headless mode
- Configuration: `src/test/resources/testng.xml`
- BaseTest creates `ChromeDriver` directly

### AWS Device Farm Platform (`platform="aws-device-farm"`)
- Uses AWS Device Farm TestGrid for remote browser execution
- Configuration: `src/test/resources/testng-aws-devicefarm.xml`
- BaseTest creates `RemoteWebDriver` with TestGrid URL
- Project ARN: `arn:aws:devicefarm:us-west-2:140023384358:testgrid-project:38759bc7-4382-461e-9fa2-e1b1e9e5eb23`
- Session expiration: 300 seconds

## Test Execution Flow

```
TestNG Suite
  ↓
FailureArtifactListeners (onStart) → Initialize ExtentReports
  ↓
BaseTest (@BeforeClass) → Initialize WebDriver (local or AWS Device Farm)
  ↓
WebDriverManager.setDriver(driver) → Store driver in singleton
  ↓
BaseTest (@BeforeTest) → initializeTestRun() [HTTP API call to test tracking service]
  ↓
Test Method Execution
  ├─→ Page Object interactions (click, type, select)
  └─→ AI validation via VisualReasonEngine
  ↓
FailureArtifactListeners.onTestSuccess/Failure() → Capture screenshot, log to ExtentReports
  ↓
BaseTest (@AfterClass) → driver.quit()
  ↓
FailureArtifactListeners.onFinish() → Generate HTML report
```

## Key Dependencies & AWS Services

**Maven Dependencies (pom.xml):**
- Selenium WebDriver 4.38.0
- TestNG 7.11.0
- AWS SDK Bedrock Runtime 2.41.5 (for AI validation)
- AWS SDK Device Farm 2.40.2 (for remote browser execution)
- ExtentReports 5.0.9 (HTML reporting)
- Jackson 2.17.2 (JSON parsing)

**AWS Services:**
- **Bedrock Runtime**: Claude Sonnet 4.5 model for visual validation (us-west-2)
- **Device Farm TestGrid**: Remote browser hosting for cross-platform testing (us-west-2)
- **ECR**: Docker image storage (`heroku-tests:latest`)

## Adding New Tests

1. **Create Page Object** (if new page):
   ```java
   public class NewPage extends BasePage {
       public NewPage(WebDriver driver) {
           super(driver);
       }
       // Define locators and interaction methods
   }
   ```

2. **Create Test Class**:
   ```java
   public class NewPageTests extends BaseTest {
       @Test
       public void testNewFeature() {
           NewPage page = new NewPage(driver);
           // Perform actions
           page.clickButton();

           // AI validation
           AiResult result = VisualReasonEngine.getInstance()
               .validateUiImage(driver, "The success message should be displayed");
           Assert.assertTrue(result.result(), result.reason());
       }
   }
   ```

3. **Add to TestNG XML**:
   ```xml
   <class name="com.herokuapp.NewPageTests"/>
   ```

## Test Listeners

**FailureArtifactListeners** (`src/test/java/com/herokuapp/listeners/FailureArtifactListeners.java`)
- Registered in TestNG XML suite configuration
- Creates ExtentTest for each test method
- Captures and embeds screenshots on test success/failure
- Generates final HTML report in `resources/Spark/Spark.html`

## Logging

- Framework: Log4j2
- Configuration: `src/main/resources/log4j2.xml`
- Console output: INFO level with timestamps and thread info
- File output: `logs/test.log`

## CI/CD Pipeline

GitHub Actions workflow (`.github/workflows/build-and-push-ecr.yml`):
1. Triggers on push to `master` or manual dispatch
2. Builds with JDK 17: `mvn clean package -DskipTests`
3. Authenticates to AWS via OIDC
4. Builds Docker image and pushes to ECR (`heroku-tests:latest`, us-west-2)

## Important Notes

- **WebDriver Lifecycle**: WebDriver is initialized once per test class (@BeforeClass) and shared across test methods. Use WebDriverManager.getInstance().getDriver() to access in helpers.
- **AI Validation Costs**: Each `VisualReasonEngine.validateUiImage()` call sends a screenshot to AWS Bedrock, incurring API costs. Use judiciously.
- **TestNG XML Selection**: The framework has multiple TestNG XML files. Always specify the correct one via `-DsuiteXmlFile` parameter.
- **AWS Credentials**: AWS services (Bedrock, Device Farm) require valid AWS credentials configured in the environment. The code uses the default AWS credential provider chain.
