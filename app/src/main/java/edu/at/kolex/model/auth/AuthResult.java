package edu.at.kolex.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class AuthResult {
    private boolean success;
    private String message;
    private AuthResponse response;
}
