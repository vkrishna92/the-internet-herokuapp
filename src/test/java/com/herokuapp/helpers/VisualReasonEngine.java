package com.herokuapp.helpers;

import com.aventstack.extentreports.ExtentTest;
import com.herokuapp.models.AiResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.List;

public class VisualReasonEngine {

    private static VisualReasonEngine instance;

    private VisualReasonEngine() {
    }

    public static VisualReasonEngine getInstance() {
        if(instance == null) {
            instance = new VisualReasonEngine();
        }
        return instance;
    }

    public AiResult validateUiImage(WebDriver webDriver, String prompt) {
        ExtentTest extentTest = ExtentReportHelper.getInstance().GetExtentTest();
        String screenshotBase64 = takeScreenshot(webDriver);
        String formattedPrompt = getInstructions() + prompt;
        extentTest.info("Input Prompt: " + prompt);
        // load image bytes
        byte[] imageBytes = new byte[0];
        try {
            imageBytes = Base64.getDecoder().decode(screenshotBase64);
        } catch (Exception e) {
            extentTest.warning("Error while parsing the image to Bytes.");
            e.printStackTrace();
        }
        
        // Image Block

        ImageSource imageSource = ImageSource.builder().bytes(SdkBytes.fromByteArray(imageBytes)).build();
        ImageBlock imageBlock = ImageBlock.builder().format(ImageFormat.PNG).source(imageSource).build();

        ContentBlock imageContent = ContentBlock.builder().image(imageBlock).build();
        ContentBlock textContent = ContentBlock.builder().text(formattedPrompt).build();

        Message message = Message.builder()
                .role(ConversationRole.USER)
                .content(List.of(imageContent, textContent))
                .build();

        String modelId = BedrockClientHelper.getInstance().getModelId();
        BedrockRuntimeClient client = BedrockClientHelper.getInstance().getClient();
        ConverseRequest request = ConverseRequest.builder().modelId(modelId).messages(message).build();

        ConverseResponse response = client.converse(request);

        String validationResult = response.output().message().content().get(0).text().trim();
        extentTest.info("LLM Output: " + validationResult);

        return parse(validationResult);
    }

    private String takeScreenshot(WebDriver driver) {
         return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    private String getInstructions() {
        return """
                You are a UI validation engine used in automated testing.
                
                You will be given:
                1) A natural language validation requirement
                2) A webpage screenshot
                3) (Optional) A DOM snapshot or metadata
                
                Your task:
                - Evaluate the requirement strictly against the provided evidence.
                - Determine whether the requirement is satisfied.
                - Provide concise reasoning based only on observable evidence.
                
                Output format (STRICT):
                Return a single valid JSON object and nothing else.
                
                The JSON object must follow this exact schema:
                {
                  "result": true or false,
                  "reason": "one concise sentence explaining the decision"
                }
                
                Rules:
                - Output ONLY the raw JSON object.
                - Do NOT wrap the JSON in markdown.
                - Do NOT use code fences such as ``` or ```json.
                - Do NOT include any text before or after the JSON.
                - Do NOT include comments or additional fields.
                - Use lowercase true or false for the result value.
                - The reason must be a single concise sentence.
                - Do NOT speculate beyond the provided evidence.
                - If the requirement cannot be conclusively validated from the evidence, set result=false and explain the uncertainty in the reason.
                - Assume default HTML semantics (e.g., checkbox checked = visually checked OR checked attribute present).
                
                Validation requirement:
                
                """;
    }

    public AiResult parse(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String cleanedJsonResponse = extractJsonObject(jsonResponse);
            JsonNode rootNode = objectMapper.readTree(cleanedJsonResponse);
            boolean result = rootNode.get("result").asBoolean();
            String reason = rootNode.get("reason").asText();

            return new AiResult(result, reason);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI validation response", e);
        }
    }
    private static String extractJsonObject(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("AI response is null");
        }

        String s = raw.trim();

        // Fast path: already JSON
        if (s.startsWith("{") && s.endsWith("}")) {
            return s;
        }

        // Remove common fenced formats if present (still keep robust extraction below)
        s = s.replaceAll("(?s)^```[a-zA-Z]*\\s*", "");  // leading ```json
        s = s.replaceAll("(?s)\\s*```$", "");           // trailing ```

        s = s.trim();

        // Robust extraction: find JSON object boundaries
        int start = s.indexOf('{');
        int end = s.lastIndexOf('}');

        if (start >= 0 && end > start) {
            return s.substring(start, end + 1).trim();
        }

        throw new IllegalArgumentException("No JSON object found in AI response: " + raw);
    }
}
