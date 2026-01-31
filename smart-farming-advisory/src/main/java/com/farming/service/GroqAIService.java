package com.farming.service;

import com.farming.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class GroqAIService {
    private static final Logger logger = LoggerFactory.getLogger(GroqAIService.class);

    /**
     * Send message to Groq API and get AI response.
     * LOGIC HIDDEN: Real API integration removed for public demo.
     */
    public String getAIResponse(List<ChatMessage> conversation, String language) throws IOException {
        logger.info("Mocking AI response for public demo.");
        
        // Return a dummy response so the code compiles and tests pass.
        // No real API call is made to save credits and hide API keys.
        return "DEMO MODE: The AI logic is hidden. In the real application, " +
               "this connects to Groq API and analyzes the farmer's query.";
    }
}
