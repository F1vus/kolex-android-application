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

    private final static boolean DEV_PROFILE = false;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = AuthRepository.getInstance();
        loginResult =  new MutableLiveData<>();
    }

    public LiveData<AuthResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        if(DEV_PROFILE){
            AuthResponse mockResponse = new AuthResponse("1","2","3","$");
            AuthResult mockResult = new AuthResult(true, "OK", mockResponse);

            TokenManager.saveToken(getApplication(), mockResponse.getToken());
            TokenManager.saveEmail(getApplication(), email);
            loginResult.postValue(mockResult);
        } else {
            authRepository.login(new LoginRequest(email, password), new AuthRepository.AuthCallback() {
                @Override
                public void onSuccess(AuthResponse response) {
                    TokenManager.saveToken(getApplication(), response.getToken());
                    TokenManager.saveEmail(getApplication(), response.getEmail());
                    loginResult.postValue(new AuthResult(true, "OK", response));
                }

                @Override
                public void onError(String message) {
                    loginResult.postValue(new AuthResult(false, message, null));
                }
            });
        }
    }
}
