package com.farming.service;

import com.farming.model.FarmerDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Send final solution email to farmer.
     * LOGIC HIDDEN: Real email sending logic removed for security.
     */
    public void sendSolutionEmail(FarmerDetails farmerDetails, String finalSolution) {
        // We log the action instead of actually sending an email.
        // This prevents the CI pipeline from failing due to missing SMTP credentials.
        logger.info("DEMO MODE: Email would be sent to {} with solution.", 
                    farmerDetails != null ? farmerDetails.getFarmerEmail() : "Unknown");
    }
}
