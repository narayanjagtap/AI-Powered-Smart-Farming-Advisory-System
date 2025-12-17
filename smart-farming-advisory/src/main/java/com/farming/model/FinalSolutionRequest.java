package com.farming.model;

public class FinalSolutionRequest {
    private String email;

    public FinalSolutionRequest() {}

    public FinalSolutionRequest(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
