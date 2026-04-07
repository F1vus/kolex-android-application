package edu.at.kolex.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.at.kolex.model.LoginRequest;
import edu.at.kolex.model.LoginResponse;
import edu.at.kolex.model.LoginResult;
import edu.at.kolex.repository.AuthRepository;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final AuthRepository authRepository = new AuthRepository();

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        authRepository.login(new LoginRequest(email, password), new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                loginResult.postValue(new LoginResult(true, "OK", response));
            }

            @Override
            public void onError(String message) {
                loginResult.postValue(new LoginResult(false, message, null));
            }
        });
    }
}
