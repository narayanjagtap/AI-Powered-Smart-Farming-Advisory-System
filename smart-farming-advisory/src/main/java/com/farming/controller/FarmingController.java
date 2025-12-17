package com.farming.controller;

import com.farming.model.*;
import com.farming.service.FarmingAdvisoryService;
import com.farming.service.FarmerSessionService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farming")
public class FarmingController {
    private static final Logger logger = LoggerFactory.getLogger(FarmingController.class);

    @Autowired
    private FarmingAdvisoryService advisoryService;

    @Autowired
    private FarmerSessionService sessionService;

    @PostMapping("/initialize")
    public ResponseEntity<?> initializeFarmer(@RequestBody FarmerDetails details, HttpSession session) {
        try {
            // Initialize session
            sessionService.initializeFarmer(details);
            logger.info("Farmer initialized successfully");
            logger.info("Session service initialized: {}", sessionService.isFarmerInitialized());

            return ResponseEntity.ok(new ChatResponse(
                    "नमस्ते " + details.getFarmerName() + "! आपकी समस्या में सहायता के लिए मैं यहाँ हूँ।\n" +
                            "(Hello " + details.getFarmerName() + "! I'm here to help you with your farming problem.)",
                    "success"
            ));
        } catch (Exception e) {
            logger.error("Error initializing farmer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ChatResponse("Error: " + e.getMessage(), "error"));
        }
    }

    @PostMapping("/chat")
    public ResponseEntity<?> sendMessage(@RequestBody ChatRequest request, HttpSession session) {
        try {
            logger.info("=== CHAT REQUEST ===");
            logger.info("Session ID: {}", session.getId());
            logger.info("Message: {}", request.getMessage());
            logger.info("Farmer initialized: {}", sessionService.isFarmerInitialized());

            if (!sessionService.isFarmerInitialized()) {
                logger.error("Session not initialized for session ID: {}", session.getId());
                return ResponseEntity.badRequest()
                        .body(new ChatResponse("Please initialize farmer details first", "error"));
            }

            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ChatResponse("Message cannot be empty", "error"));
            }

            String aiResponse = advisoryService.getChatResponse(request.getMessage());
            logger.info("AI response generated successfully");

            return ResponseEntity.ok(new ChatResponse(aiResponse, "success"));
        } catch (Exception e) {
            logger.error("Error in chat: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ChatResponse("Error: " + e.getMessage(), "error"));
        }
    }

    
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(new ChatResponse("Smart Farming Advisory System is running", "success"));
    }
}