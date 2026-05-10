package edu.at.kolex.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import edu.at.kolex.model.Profile;
import edu.at.kolex.repository.UserRepository;

public class ProfilesViewModel extends ViewModel {

    private final UserRepository repository = UserRepository.getInstance();

    private final LiveData<List<Profile>> profiles = repository.getProfilesCache();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();

    public LiveData<List<Profile>> getProfiles() {
        return profiles;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public void loadProfiles() {
        loading.setValue(true);

        repository.loadProfilesIfNeeded(new UserRepository.ProfileListCallback() {
            @Override
            public void onSuccess(List<Profile> data) {
                loading.setValue(false);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                error.setValue(message);
            }
        });
    }

    public void deleteProfile(Long id) {
        loading.setValue(true);

        repository.deleteProfile(id, new UserRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
                successMessage.setValue("Profil usunięty");
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                error.setValue(message);
            }
        });
    }

    public void saveProfile(@Nullable Profile existingProfile, String firstName, String lastName) {
        loading.setValue(true);

        if (existingProfile == null) {
            Profile newProfile = new Profile(null, firstName, lastName);

            repository.createProfile(newProfile, new UserRepository.ProfileCallback() {
                @Override
                public void onSuccess(Profile profile) {
                    loading.setValue(false);
                    successMessage.setValue("Profil dodany pomyślnie");
                }

                @Override
                public void onError(String message) {
                    loading.setValue(false);
                    error.setValue(message);
                }
            });
        } else {
            Profile updatedProfile = new Profile(
                    existingProfile.getId(),
                    firstName,
                    lastName
            );

            repository.updateProfile(updatedProfile, new UserRepository.ProfileCallback() {
                @Override
                public void onSuccess(Profile profile) {
                    loading.setValue(false);
                    successMessage.setValue("Profil zaktualizowany pomyślnie");
                }

                @Override
                public void onError(String message) {
                    loading.setValue(false);
                    error.setValue(message);
                }
            });
        }
    }

    public void clearMessages() {
        error.setValue(null);
        successMessage.setValue(null);
    }
}