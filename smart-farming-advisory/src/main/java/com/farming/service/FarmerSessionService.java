package com.farming.service;

import com.farming.model.ChatMessage;
import com.farming.model.FarmerDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class FarmerSessionService {
    private FarmerDetails farmerDetails;
    private List<ChatMessage> conversationHistory;

    public FarmerSessionService() {
        this.conversationHistory = new ArrayList<>();
    }

    /**
     * Initialize farmer session
     */
    public void initializeFarmer(FarmerDetails details) {
        this.farmerDetails = details;
        this.conversationHistory = new ArrayList<>();
    }

    /**
     * Add user message to conversation
     */
    public void addUserMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            conversationHistory.add(new ChatMessage("user", message));
        }
    }

    /**
     * Add assistant response to conversation
     */
    public void addAssistantMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            conversationHistory.add(new ChatMessage("assistant", message));
        }
    }

    /**
     * Get all conversation history
     */
    public List<ChatMessage> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }

    /**
     * Get farmer details
     */
    public FarmerDetails getFarmerDetails() {
        return farmerDetails;
    }

    /**
     * Check if farmer details are set
     */
    public boolean isFarmerInitialized() {
        return farmerDetails != null;
    }

    /**
     * Clear session
     */
    public void clearSession() {
        this.farmerDetails = null;
        this.conversationHistory.clear();
    }
}
