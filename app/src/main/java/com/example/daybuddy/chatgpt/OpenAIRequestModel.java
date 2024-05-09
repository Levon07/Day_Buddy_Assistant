package com.example.daybuddy.chatgpt;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class OpenAIRequestModel {
    @SerializedName("model")
    private String model;
    @SerializedName("messages")
    private List<Message> messages;
    @SerializedName("temperature")
    private float temperature;
    public OpenAIRequestModel(String model, List<Message> messages, float temperature) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
    }
}
class Message {
    @SerializedName("role")
    private String role;
    @SerializedName("content")
    private String content;
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }
}