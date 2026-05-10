package edu.at.kolex.model.auth.register;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterRequest {
    private String email;
    private String password;
    private String confirmPassword;
}