package com.farming.model;

public class FarmerDetails {
    private String farmerName;
    private String farmerEmail;
    private String problemType;
    private String preferredLanguage;

    // Constructors
    public FarmerDetails() {}

    public FarmerDetails(String farmerName, String farmerEmail, String problemType, String preferredLanguage) {
        this.farmerName = farmerName;
        this.farmerEmail = farmerEmail;
        this.problemType = problemType;
        this.preferredLanguage = preferredLanguage;
    }

    // Getters and Setters
    public String getFarmerName() { return farmerName; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }

    public String getFarmerEmail() { return farmerEmail; }
    public void setFarmerEmail(String farmerEmail) { this.farmerEmail = farmerEmail; }

    public String getProblemType() { return problemType; }
    public void setProblemType(String problemType) { this.problemType = problemType; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
}
