package edu.at.kolex.api;

import edu.at.kolex.model.auth.AuthResponse;
import edu.at.kolex.model.auth.login.LoginRequest;
import edu.at.kolex.model.auth.register.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);
}
