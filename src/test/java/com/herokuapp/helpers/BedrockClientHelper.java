package com.herokuapp.helpers;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ImageBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ImageFormat;

public class BedrockClientHelper {

    private static final String MODEL_ID = "us.anthropic.claude-sonnet-4-5-20250929-v1:0";
    private static final Region REGION = Region.US_WEST_2;
    private static BedrockClientHelper bedrockClient;


    private BedrockRuntimeClient client;
    private BedrockClientHelper() {

        try {
            client = BedrockRuntimeClient.builder().region(REGION).build();
        } catch (Exception ex) {
            System.out.println("Error while creating Bedrock Client");
            ex.printStackTrace();
        }
    }

    public static BedrockClientHelper getInstance() {
        if (bedrockClient == null) {
            bedrockClient = new BedrockClientHelper();
        }

        return bedrockClient;
    }

    public String getModelId() {
        return MODEL_ID;
    }

    public BedrockRuntimeClient getClient() {
        return client;
    }


}
