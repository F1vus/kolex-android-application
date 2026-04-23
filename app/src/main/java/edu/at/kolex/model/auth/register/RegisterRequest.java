package edu.at.kolex.model.auth.register;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterRequest {
    private String email;
    private String password;
    @SerializedName("confirm_password")
    private String passwordConfirm;
}