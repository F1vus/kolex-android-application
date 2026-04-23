package edu.at.kolex.model.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AuthResult {
    private final boolean success;
    private final String message;
    private final AuthResponse response;
}
