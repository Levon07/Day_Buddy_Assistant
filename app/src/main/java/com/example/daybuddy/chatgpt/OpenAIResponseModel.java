package com.example.daybuddy.chatgpt;

import com.google.gson.annotations.SerializedName;
public class OpenAIResponseModel {
    @SerializedName("id")
    private String id;
    @SerializedName("object")
    private String objectType;
    @SerializedName("created")
    private long createdTimestamp;
    @SerializedName("model")
    private String modelVersion;
    @SerializedName("choices")
    private OpenAIChoice[] choices;
    @SerializedName("usage")
    private Usage usageInfo;
    public String getId() {
        return id;
    }
    public String getObjectType() {
        return objectType;
    }
    public long getCreatedTimestamp() {
        return createdTimestamp;
    }
    public String getModelVersion() {
        return modelVersion;
    }
    public OpenAIChoice[] getChoices() {
        return choices;
    }
    public Usage getUsageInfo() {
        return usageInfo;
    }
}
class OpenAIChoice {
    @SerializedName("message")
    private ResponseMessage message;
    @SerializedName("finish_reason")
    private String finishReason;
    public ResponseMessage getMessage() {
        return message;
    }
    public String getFinishReason() {
        return finishReason;
    }
}
class ResponseMessage {
    @SerializedName("role")
    private String role;
    @SerializedName("content")
    private String content;
    public String getRole() {
        return role;
    }
    public String getContent() {
        return content;
    }
}
class Usage {
    @SerializedName("prompt_tokens")
    private int promptTokens;
    @SerializedName("completion_tokens")
    private int completionTokens;
    @SerializedName("total_tokens")
    private int totalTokens;
    public int getPromptTokens() {
        return promptTokens;
    }
    public int getCompletionTokens() {
        return completionTokens;
    }
    public int getTotalTokens() {
        return totalTokens;
    }
}