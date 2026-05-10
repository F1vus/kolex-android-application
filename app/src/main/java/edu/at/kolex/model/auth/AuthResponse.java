package edu.at.kolex.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String type;
    private String email;
}