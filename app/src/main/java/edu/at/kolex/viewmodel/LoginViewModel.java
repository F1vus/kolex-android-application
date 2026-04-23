package edu.at.kolex.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import edu.at.kolex.model.auth.AuthResponse;
import edu.at.kolex.model.auth.AuthResult;
import edu.at.kolex.model.auth.login.LoginRequest;
import edu.at.kolex.repository.AuthRepository;
import edu.at.kolex.utils.TokenManager;

public class LoginViewModel extends AndroidViewModel {

    private final MutableLiveData<AuthResult> loginResult;
    private final AuthRepository authRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = AuthRepository.getInstance();
        loginResult =  new MutableLiveData<>();
    }

    public LiveData<AuthResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        authRepository.login(new LoginRequest(email, password), new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse response) {
                TokenManager.saveToken(getApplication(), response.getToken());
                loginResult.postValue(new AuthResult(true, "OK", response));
            }

            @Override
            public void onError(String message) {
                loginResult.postValue(new AuthResult(false, message, null));
            }
        });
    }
}
