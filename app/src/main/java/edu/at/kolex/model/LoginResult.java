package edu.at.kolex.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginResult {
    private final boolean success;
    private final String message;
    private final LoginResponse response;
}
