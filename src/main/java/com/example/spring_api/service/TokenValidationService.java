package com.example.spring_api.service;

import com.example.spring_api.client.UserServiceClient;
import com.example.spring_api.dto.TokenValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TokenValidationService {

    @Autowired
    private UserServiceClient userServiceClient;

    public TokenValidationResponse validateAdminToken(String authorizationHeader) {
        TokenValidationResponse response = new TokenValidationResponse();
        
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setValid(false);
            response.setAdmin(false);
            response.setMessage("Invalid authorization header");
            return response;
        }

        try {
            ResponseEntity<String> adminResponse = userServiceClient.validateAdmin(authorizationHeader);
            
            if (adminResponse.getStatusCode() == HttpStatus.OK) {
                response.setValid(true);
                response.setAdmin(true);
                response.setMessage("User is an administrator");
            } else {
                response.setValid(false);
                response.setAdmin(false);
                response.setMessage("User is not an administrator");
            }
        } catch (Exception e) {
            response.setValid(false);
            response.setAdmin(false);
            response.setMessage("Error validating token: " + e.getMessage());
        }
        
        return response;
    }
} 