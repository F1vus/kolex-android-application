package edu.at.kolex.api;

import edu.at.kolex.model.LoginRequest;
import edu.at.kolex.model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
