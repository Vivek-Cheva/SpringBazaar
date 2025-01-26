package com.example.ecom_proj.controller;

import com.example.ecom_proj.DTO.LoginRequest;
import com.example.ecom_proj.DTO.SignupRequest;
import com.example.ecom_proj.model.User;
import com.example.ecom_proj.repository.UserRepository;
import com.example.ecom_proj.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        System.out.println("Received signup request: " + signupRequest); // Log request
        try {
            String message = authService.register(signupRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            System.err.println("Error during signup: " + e.getMessage()); // Log error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


}
