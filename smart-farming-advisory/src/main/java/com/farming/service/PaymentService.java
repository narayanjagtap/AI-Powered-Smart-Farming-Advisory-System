package com.farming.service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.farming.model.Payment;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private RazorpayClient razorpayClient;

    public PaymentService(@Value("${razorpay.key.id}") String keyId,
                          @Value("${razorpay.key.secret}") String keySecret) throws RazorpayException {
        this.razorpayKeyId = keyId;
        this.razorpayKeySecret = keySecret;
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
    }

    // Create Razorpay Order
    public String createRazorpayOrder(double amount, String email, String phoneNumber) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (long) (amount * 100)); // Amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "receipt_" + System.currentTimeMillis());
        orderRequest.put("notes", new JSONObject()
                .put("email", email)
                .put("phone", phoneNumber));

        com.razorpay.Order order = razorpayClient.Orders.create(orderRequest);
        return order.get("id");
    }

    // Verify Payment Signature
    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) throws Exception {
        String data = orderId + "|" + paymentId;
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(razorpayKeySecret.getBytes(), 0, 
                                                     razorpayKeySecret.getBytes().length, "HmacSHA256");
        mac.init(secretKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        String generatedSignature = bytesToHex(rawHmac);
        return generatedSignature.equals(signature);
    }

    // Save Payment Record
    public void savePaymentRecord(String orderId, String paymentId, String email, double amount) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setPaymentId(paymentId);
        payment.setEmail(email);
        payment.setAmount(amount);
        payment.setStatus("completed");
        payment.setCreatedAt(new Date());
        // Save to database or session as per your requirement
        // paymentRepository.save(payment);
    }

    // Get Payment Status
    public Payment getPaymentStatus(String paymentId) throws RazorpayException {
        com.razorpay.Payment payment = razorpayClient.Payments.fetch(paymentId);
        Payment paymentRecord = new Payment();
        paymentRecord.setPaymentId(paymentId);
        paymentRecord.setStatus(payment.get("status"));
        paymentRecord.setAmount(Double.parseDouble(payment.get("amount").toString()) / 100);
        return paymentRecord;
    }

    // Utility method to convert bytes to hex
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
