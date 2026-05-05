package edu.at.kolex.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthResult {
    private boolean success;
    private String message;
    private AuthResponse response;
}
