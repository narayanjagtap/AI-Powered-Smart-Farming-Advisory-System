package com.farming.controller;

import com.farming.model.Payment;
import com.farming.service.PaymentService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Create Razorpay Order
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Payment payment) {
        try {
            String orderId = paymentService.createRazorpayOrder(payment.getAmount(), payment.getEmail(), payment.getPhoneNumber());
            return ResponseEntity.ok(new PaymentResponse("success", orderId, payment.getAmount()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentResponse("error", null, 0.0));
        }
    }

    // Verify Payment
    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        try {
            boolean isValid = paymentService.verifyPaymentSignature(
                    request.getOrderId(),
                    request.getPaymentId(),
                    request.getSignature()
            );
            
            if (isValid) {
                paymentService.savePaymentRecord(request.getOrderId(), request.getPaymentId(), 
                                                 request.getEmail(), request.getAmount());
                return ResponseEntity.ok(new PaymentResponse("success", request.getPaymentId(), request.getAmount()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new PaymentResponse("error", "Payment verification failed", 0.0));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentResponse("error", e.getMessage(), 0.0));
        }
    }

    // Get Payment Status
    @GetMapping("/status/{paymentId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String paymentId) {
        try {
            Payment payment = paymentService.getPaymentStatus(paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PaymentResponse("error", "Payment not found", 0.0));
        }
    }
}

class PaymentResponse {
    private String status;
    private Object data;
    private double amount;

    public PaymentResponse(String status, Object data, double amount) {
        this.status = status;
        this.data = data;
        this.amount = amount;
    }

    public String getStatus() { return status; }
    public Object getData() { return data; }
    public double getAmount() { return amount; }
}

class PaymentVerificationRequest {
    private String orderId;
    private String paymentId;
    private String signature;
    private String email;
    private double amount;

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}