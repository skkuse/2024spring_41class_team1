package com.skku.BitCO2e.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ReviewDTO {
    private String status;

    // Default constructor
    public ReviewDTO() {
    }

    // Parameterized constructor
    public ReviewDTO(String status) {
        this.status = status;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }

}
