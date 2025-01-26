package com.example.ecom_proj.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
