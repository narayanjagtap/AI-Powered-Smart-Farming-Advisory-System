package com.farming.service;

import com.farming.model.ChatMessage;
import com.farming.model.FarmerDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FarmingAdvisoryService {
    private static final Logger logger = LoggerFactory.getLogger(FarmingAdvisoryService.class);

    @Autowired
    private GroqAIService groqAIService;

    @Autowired
    private FarmerSessionService sessionService;

    @Autowired
    private EmailService emailService;

    /**
     * Get AI response for user message
     */
    public String getChatResponse(String userMessage) throws IOException {
        logger.info("Processing chat request for user message");

        if (!sessionService.isFarmerInitialized()) {
            logger.error("Farmer details not initialized");
            throw new IllegalStateException("Farmer details not initialized");
        }

        // Validate user message
        if (userMessage == null || userMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("User message cannot be empty");
        }

        // Add user message to history
        sessionService.addUserMessage(userMessage);

        // Get conversation history
        List<ChatMessage> history = sessionService.getConversationHistory();
        String language = sessionService.getFarmerDetails().getPreferredLanguage();

        logger.info("Calling Groq AI Service with {} messages in conversation", history.size());

        // Get AI response
        String aiResponse = groqAIService.getAIResponse(history, language);

        // Add AI response to history
        sessionService.addAssistantMessage(aiResponse);

        logger.info("Chat response generated for farmer: {}",
                sessionService.getFarmerDetails().getFarmerName());

        return aiResponse;
    }

    /**
     * Generate final solution and send email
     */
    public String generateFinalSolutionAndSendEmail() throws IOException {
        logger.info("Generating final solution");

        if (!sessionService.isFarmerInitialized()) {
            logger.error("Farmer details not initialized for final solution");
            throw new IllegalStateException("Farmer details not initialized");
        }

        FarmerDetails farmerDetails = sessionService.getFarmerDetails();
        List<ChatMessage> history = sessionService.getConversationHistory();
        String language = farmerDetails.getPreferredLanguage();

        // Create final summary request
        List<ChatMessage> summaryMessages = new ArrayList<>(history);

        String summaryPrompt = "Based on our entire conversation, provide a comprehensive final summary including: " +
                "1. Cause of the problem, " +
                "2. Recommended solution, " +
                "3. Precautions to take. " +
                "Make it clear and actionable for a farmer. " +
                "Format it in simple, easy-to-understand points.";

        summaryMessages.add(new ChatMessage("user", summaryPrompt));

        logger.info("Calling Groq AI for final solution with {} messages", summaryMessages.size());

        // Get final solution from AI
        String finalSolution = groqAIService.getAIResponse(summaryMessages, language);

        logger.info("Sending email to farmer: {}", farmerDetails.getFarmerEmail());

        // Send email
        emailService.sendSolutionEmail(farmerDetails, finalSolution);

        logger.info("Final solution generated and email sent successfully to: {}",
                farmerDetails.getFarmerEmail());

        return finalSolution;
    }
}
