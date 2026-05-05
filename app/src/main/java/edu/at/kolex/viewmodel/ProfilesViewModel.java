package edu.at.kolex.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import edu.at.kolex.model.ProfileDTO;
import edu.at.kolex.repository.UserRepository;

public class ProfilesViewModel extends ViewModel {

    private final UserRepository repository = UserRepository.getInstance();

    private final MutableLiveData<List<ProfileDTO>> profiles = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public LiveData<List<ProfileDTO>> getProfiles() {
        return profiles;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void loadProfiles() {
        loading.setValue(true);

        repository.getUserProfiles(new UserRepository.ProfileListCallback() {
            @Override
            public void onSuccess(List<ProfileDTO> data) {
                loading.setValue(false);
                profiles.setValue(data);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
            }
        });
    }
}
