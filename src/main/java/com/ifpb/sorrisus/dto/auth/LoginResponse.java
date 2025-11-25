package com.ifpb.sorrisus.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresIn;
    private int userId;
    private String email;
    private String role;
}
