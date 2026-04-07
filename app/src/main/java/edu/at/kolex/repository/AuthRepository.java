package edu.at.kolex.repository;

import androidx.annotation.NonNull;

import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.AuthApiService;
import edu.at.kolex.model.LoginRequest;
import edu.at.kolex.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthRepository {

    private final AuthApiService authApiService = ApiClient.getClient().create(AuthApiService.class);

    public interface LoginCallback {
        void onSuccess(LoginResponse response);
        void onError(String message);
    }

    public void login(LoginRequest request, LoginCallback callback) {
        authApiService.login(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Błąd logowania: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }
}