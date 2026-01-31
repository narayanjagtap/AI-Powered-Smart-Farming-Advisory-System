package com.farming.service;

import com.farming.model.FarmerDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
        logger.info("Processing chat request (Demo Mode)");
        
        // Validate user message simply
        if (userMessage == null || userMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("User message cannot be empty");
        }

        // Return dummy data
        return "This is a simulated response. The real AI service is hidden.";
    }

    /**
     * Generate final solution and send email
     */
    public String generateFinalSolutionAndSendEmail() throws IOException {
        logger.info("Generating final solution (Demo Mode)");

        if (!sessionService.isFarmerInitialized()) {
             // Basic check to keep logic realistic
             logger.warn("Session not initialized");
        }

        // Mock the email sending process
        String dummySolution = "Final Solution Summary (Hidden)";
        
        // We call the email service (which is also hollow) so the flow remains valid
        if (sessionService.getFarmerDetails() != null) {
            emailService.sendSolutionEmail(sessionService.getFarmerDetails(), dummySolution);
        }

        return dummySolution;
    }
}
