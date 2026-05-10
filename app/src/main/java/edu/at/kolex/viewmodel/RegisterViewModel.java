package edu.at.kolex.viewmodel;


import static android.provider.Settings.System.getString;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import edu.at.kolex.R;
import edu.at.kolex.model.auth.AuthResponse;
import edu.at.kolex.model.auth.AuthResult;
import edu.at.kolex.model.auth.register.RegisterRequest;
import edu.at.kolex.repository.AuthRepository;
import edu.at.kolex.utils.TokenManager;

public class RegisterViewModel extends AndroidViewModel {

    private final MutableLiveData<AuthResult> registerResult;
    private final AuthRepository authRepository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = AuthRepository.getInstance();
        this.registerResult = new MutableLiveData<>();
    }

    public void register(String email, String password, String passwordConfirm) {
        authRepository.register(new RegisterRequest(email, password, passwordConfirm), new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse response) {
                TokenManager.saveToken(getApplication(), response.getToken());
                registerResult.postValue(new AuthResult(true, getString(R.string.ok___), response));
            }

            private String getString(int ok___) {
                return "";
            }

            @Override
            public void onError(String message) {
                registerResult.postValue(new AuthResult(false, message, null));
            }
        });
    }

    public LiveData<AuthResult> getRegisterResult() {
        return registerResult;
    }
}
