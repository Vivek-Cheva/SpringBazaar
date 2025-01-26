package com.example.ecom_proj.service;

import com.example.ecom_proj.DTO.SignupRequest;
import com.example.ecom_proj.model.User;
import com.example.ecom_proj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;


@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    public String register(SignupRequest signupRequest) {
        Optional<User> existingUser = userRepository.findByEmail(signupRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword()); // Hash in production
        userRepository.save(user);

        return "User registered successfully";
    }

    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate a dummy token (replace with JWT in production)
        return Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
    }
}
