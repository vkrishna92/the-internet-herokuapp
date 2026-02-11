package com.herokuapp.models;

public class AiResult {
    private final boolean result;
    private final String reason;

    public AiResult(boolean result, String reason) {
        this.result = result;
        this.reason = reason;
    }

    public boolean isResult() {
        return result;
    }

    public String getReason() {
        return reason;
    }
}
