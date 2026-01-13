package com.herokuapp.helpers;

import org.apache.logging.log4j.core.util.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public boolean validateUiImage(WebDriver webDriver, String prompt) {

        String screenshotBase64 = takeScreenshot(webDriver);
        String formattedPrompt = getInstructions() + prompt;
        System.out.println("Input Prompt: " + prompt);
        // load image bytes
        byte[] imageBytes = new byte[0];
        try {
            imageBytes = Base64.getDecoder().decode(screenshotBase64);
        } catch (Exception e) {
            System.out.println("Error while parsing the image to Bytes.");
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
        System.out.println("LLM Output: " + validationResult);

        return Boolean.parseBoolean(validationResult);
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
                - Return ONLY one of the following lowercase values:
                  true
                  false
                
                Rules:
                - Do NOT explain.
                - Do NOT add punctuation.
                - Do NOT add quotes.
                - Do NOT add code blocks.
                - If the requirement cannot be conclusively validated from the evidence, return false.
                - Assume default HTML semantics (e.g., checkbox checked = visually checked OR checked attribute present).
                
                Validation requirement:
                """;
    }


}
