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
            

            return jsonResponse
                    .getAsJsonArray("choices")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content")
                    .getAsString();
        }
    }


}
