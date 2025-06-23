package com.example.spring_api.dto;

import lombok.Data;

@Data
public class TokenValidationResponse {
    private boolean valid;
    private boolean isAdmin;
    private String message;
    
    // Getters
    public boolean isValid() {
        return valid;
    }
    
    public boolean isAdmin() {
        return isAdmin;
    }
    
    public String getMessage() {
        return message;
    }
    
    // Setters
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
} 