package com.farming.service;

import com.farming.model.FarmerDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    // Injected property with the display name format
    @Value("${app.mail.display-name:Smart Farming <noreply@smartfarming.com>}")
    private String fromDisplayName;

    /**
     * Send final solution email to farmer with a clear display name
     */
    public void sendSolutionEmail(FarmerDetails farmerDetails, String finalSolution) {
        try {
            // Validate email before sending
            if (farmerDetails.getFarmerEmail() == null || !isValidEmail(farmerDetails.getFarmerEmail())) {
                throw new IllegalArgumentException("Invalid farmer email: " + farmerDetails.getFarmerEmail());
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(farmerDetails.getFarmerEmail());
            message.setSubject("Your Farming Advisory Solution - " + farmerDetails.getProblemType());

            // This is the critical line. It now sends "Name <email@domain.com>"
            message.setFrom(fromDisplayName);

            message.setText(buildEmailContent(farmerDetails, finalSolution));

            mailSender.send(message);
            logger.info("Email sent successfully to: {} using display name: {}",
                    farmerDetails.getFarmerEmail(), fromDisplayName);

        } catch (Exception e) {
            logger.error("Failed to send email: {}", e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    /**
     * Build email content for final farming advisory
     */
    private String buildEmailContent(FarmerDetails farmerDetails, String solution) {

        String name = farmerDetails.getFarmerName();
        String language = farmerDetails.getPreferredLanguage();

        String greeting;
        String intro;
        String closing;

        switch (language) {
            case "HINDI" -> {
                greeting = "नमस्ते " + name + ",";
                intro = "आपकी समस्या के आधार पर तैयार किया गया अंतिम समाधान नीचे दिया गया है।";
                closing = "धन्यवाद,\nस्मार्ट फार्मिंग एडवाइजरी सिस्टम";
            }
            case "MARATHI" -> {
                greeting = "नमस्कार " + name + ",";
                intro = "आपल्या समस्येच्या आधारे तयार केलेला अंतिम सल्ला खाली दिला आहे.";
                closing = "धन्यवाद,\nस्मार्ट फार्मिंग अ‍ॅडव्हायझरी सिस्टम";
            }
            default -> {
                greeting = "Hello " + name + ",";
                intro = "Based on your discussion, here is the final farming advisory for your problem.";
                closing = "Thank you,\nSmart Farming Advisory System";
            }
        }

        StringBuilder content = new StringBuilder();

        content.append(greeting).append("\n\n");
        content.append(intro).append("\n\n");

        content.append("--------------------------------------------------\n");
        content.append(solution).append("\n");
        content.append("--------------------------------------------------\n\n");

        content.append("If you have more questions, feel free to use the Smart Farming Advisory System again.\n\n");
        content.append(closing);

        return content.toString();
    }

}