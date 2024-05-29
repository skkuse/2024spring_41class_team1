package com.skku.BitCO2e.model;

public class Advertisement {
    private String username;
    private String currentBit;
    private String usedBit;
    private String message;
    private String imageUrl;
    private String status;
    private String date;

    public Advertisement() {
        // Default constructor required for calls to DataSnapshot.getValue(Advertisement.class)
    }

    public Advertisement(String username, String currentBit, String usedBit, String message, String imageUrl, String status, String date) {
        this.username = username;
        this.currentBit = currentBit;
        this.usedBit = usedBit;
        this.message = message;
        this.imageUrl = imageUrl;
        this.status = status;
        this.date = date;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getCurrentBit() { return currentBit; }
    public void setCurrentBit(String currentBit) { this.currentBit = currentBit; }
    public String getUsedBit() { return usedBit; }
    public void setUsedBit(String usedBit) { this.usedBit = usedBit; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}