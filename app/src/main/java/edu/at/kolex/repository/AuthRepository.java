package edu.at.kolex.repository;

import android.util.Log;
import androidx.annotation.NonNull;

import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.AuthApiService;
import edu.at.kolex.model.auth.AuthResponse;
import edu.at.kolex.model.auth.login.LoginRequest;

import edu.at.kolex.model.auth.register.RegisterRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthRepository {

    private final AuthApiService authApiService = ApiClient.getClient().create(AuthApiService.class);

    private static AuthRepository instance;

    public static AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    public interface AuthCallback {
        void onSuccess(AuthResponse response);
        void onError(String message);
    }

    public void login(LoginRequest request, AuthCallback callback) {
        authApiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Błąd logowania: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                Log.v("AuthRepository", t.getLocalizedMessage(), t);
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }

    public void register(RegisterRequest request, AuthCallback callback) {
        authApiService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Błąd rejestracji: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                Log.v("AuthRepository", t.getLocalizedMessage(), t);
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }
}