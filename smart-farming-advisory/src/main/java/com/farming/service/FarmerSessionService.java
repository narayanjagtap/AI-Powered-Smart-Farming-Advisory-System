package com.farming.service;

import com.farming.model.ChatMessage;
import com.farming.model.FarmerDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@SessionScope
public class FarmerSessionService {
    
    // Simple placeholder to allow compilation
    private FarmerDetails farmerDetails;

    public void initializeFarmer(FarmerDetails details) {
        this.farmerDetails = details;
    }

    public void addUserMessage(String message) {
        // Logic hidden
    }

    public void addAssistantMessage(String message) {
        // Logic hidden
    }

    public List<ChatMessage> getConversationHistory() {
        return Collections.emptyList();
    }

    public FarmerDetails getFarmerDetails() {
        return farmerDetails;
    }

    public boolean isFarmerInitialized() {
        return farmerDetails != null;
    }

    public void clearSession() {
        this.farmerDetails = null;
    }
}
