package com.farming.model;

import java.time.LocalDateTime;

/**
 * Payment model class representing payment information in the Smart Farming Advisory System
 */
public class Payment {
    private Long paymentId;
    private Long orderId;
    private String email;
    private String phoneNumber;
    private Double amount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public Payment() {
    }

    /**
     * Constructor with all fields
     */
    public Payment(Long paymentId, Long orderId, String email, String phoneNumber, 
                   Double amount, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters

    /**
     * Get payment ID
     * @return paymentId
     */
    public Long getPaymentId() {
        return paymentId;
    }

    /**
     * Set payment ID
     * @param paymentId the payment ID to set
     */
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * Get order ID
     * @return orderId
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * Set order ID
     * @param orderId the order ID to set
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * Get email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get phone number
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set phone number
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get amount
     * @return amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Set amount
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Get status
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get created at timestamp
     * @return createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Set created at timestamp
     * @param createdAt the created at timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get updated at timestamp
     * @return updatedAt
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set updated at timestamp
     * @param updatedAt the updated at timestamp to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * String representation of Payment object
     */
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
