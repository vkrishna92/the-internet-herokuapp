# The Internet Herokuapp Test Automation

A Selenium WebDriver test automation framework for testing [The Internet](https://the-internet.herokuapp.com/) application.

## Tech Stack

- **Java 11**
- **Selenium WebDriver 4.38.0**
- **TestNG 7.11.0**
- **Maven** (Build tool)
- **Log4j2** (Logging)
- **Docker** (Containerization)

## Project Structure

```
├── src/
│   ├── main/java/          # Page Object classes
│   └── test/
│       ├── java/           # Test classes
│       └── resources/      # TestNG XML files
├── .github/workflows/      # CI/CD pipeline
├── Dockerfile             # Docker configuration
└── pom.xml               # Maven dependencies
```

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Chrome browser (for local execution)

## Running Tests

### Local Execution
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Docker Execution
```bash
docker build -t selenium-tests .
docker run selenium-tests
```

## CI/CD Pipeline

The project includes a GitHub Actions workflow that:
- Builds the project with Maven
- Creates a Docker image
- Pushes to AWS ECR repository

## AWS Integration

- **ECR Repository**: `selenium-tests`
- **Region**: `us-west-2`
- **Authentication**: OIDC with GitHub Actions

## Test Coverage

Currently includes tests for:
- Add/Remove Elements functionality

## Logs

Test execution logs are stored in the `logs/` directory.