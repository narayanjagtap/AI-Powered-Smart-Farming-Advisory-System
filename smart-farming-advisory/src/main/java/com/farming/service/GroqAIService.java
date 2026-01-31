package com.farming.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.*;

import java.io.IOException;

@Service
public class GroqAIService {
    private static final Logger logger = LoggerFactory.getLogger(GroqAIService.class);
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    @Value("${groq.api.key:sk-default-key}")
    private String apiKey;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    /**
     * Send message to Groq API and get AI response
     */
    public String getAIResponse(java.util.List<com.farming.model.ChatMessage> conversation, String language) throws IOException {
        // Validate API key
        if (apiKey == null || apiKey.startsWith("sk-default")) {
            logger.warn("WARNING: Using default API key. Please set GROQ_API_KEY environment variable.");
            throw new IOException("Groq API key not configured. Please set GROQ_API_KEY environment variable.");
        }

        JsonObject requestBody = buildRequestBody(conversation, language);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            // HANDLE RATE LIMIT (429)
            if (response.code() == 429) {
                logger.warn("Groq API rate limit reached (429). User should retry later.");
                return "⚠️ System is busy right now. Please wait 10–15 minutes and try again.";
            }

            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                logger.error("Groq API Error: " + response.code() + " " + errorBody);
                throw new IOException("Failed to get response from Groq API: " + response.code());
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

            if (jsonResponse == null || !jsonResponse.has("choices")) {
                throw new IOException("Invalid response format from Groq API");
            }

            return jsonResponse
                    .getAsJsonArray("choices")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content")
                    .getAsString();
        }
    }

    /**
     * Build request body for Groq API
     */
    private JsonObject buildRequestBody(java.util.List<com.farming.model.ChatMessage> conversation, String language) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model);
        requestBody.addProperty("temperature", 0.7);
        requestBody.addProperty("max_tokens", 500);

        JsonArray messages = new JsonArray();

        // System prompt with language instruction
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", buildSystemPrompt(language));
        messages.add(systemMessage);

        // Add conversation history
        for (com.farming.model.ChatMessage msg : conversation) {
            JsonObject msgObj = new JsonObject();
            msgObj.addProperty("role", msg.getRole());
            msgObj.addProperty("content", msg.getContent());
            messages.add(msgObj);
        }

        requestBody.add("messages", messages);
        return requestBody;
    }

    /**
     * Build system prompt based on language
     */
    private String buildSystemPrompt(String language) {

        String languageInstruction = switch (language) {
            case "HINDI" ->
                    "उत्तर केवल सरल और स्पष्ट हिंदी में दें। तालिका, चिन्ह (**, ##, |) या जटिल शब्दों का उपयोग न करें।";
            case "MARATHI" ->
                    "उत्तर फक्त सोप्या आणि स्पष्ट मराठीत द्या. टेबल, **, ##, | असे चिन्ह वापरू नका.";
            default ->
                    "Respond only in simple and clear English. Do NOT use tables, markdown symbols, or special formatting.";
        };

        return """
You are a trusted agricultural expert and advisor for Indian farmers.

Your advice must be:
- Practical and based on real Indian farming practices
- Easy to understand for rural farmers
- Suitable for small and medium landholding farmers

You can help farmers with:
- Crop problems and growth issues
- Irrigation and water management
- Fertilizer usage (organic and chemical)
- Pest and disease control
- Weather-related precautions
- Government schemes and subsidies

IMPORTANT RESPONSE RULES (STRICT):
- DO NOT use tables or tabular format
- DO NOT use markdown symbols like **, ##, |, or *
- DO NOT highlight text using bold or special characters
- Use only plain text
- Use short paragraphs
- Prefer numbered steps (1. 2. 3.) or simple bullet points (-)
- Keep each point on a new line
- Avoid complex scientific terms; explain in simple words
- If exact information is missing, ask one simple follow-up question

Tone and style:
- Friendly, respectful, and supportive
- Like an experienced local agriculture officer
- Never give dangerous, extreme, or unverified advice

""" + languageInstruction;
    }


}
