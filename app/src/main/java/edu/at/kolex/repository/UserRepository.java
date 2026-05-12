package edu.at.kolex.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.UserApiService;
import edu.at.kolex.model.Profile;
import edu.at.kolex.model.UserBalanceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private static UserRepository instance;

    private final UserApiService apiService;

    private final MutableLiveData<List<Profile>> profilesCache =
            new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Profile>> getProfilesCache() {
        return profilesCache;
    }

    private UserRepository() {
        apiService = ApiClient.getClient().create(UserApiService.class);
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public interface BalanceCallback {
        void onSuccess(BigDecimal balance);
        void onError(String message);
    }

    public interface ProfileListCallback {
        void onSuccess(List<Profile> data);
        void onError(String message);
    }

    public interface ProfileCallback {
        void onSuccess(Profile profile);
        void onError(String message);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    public boolean hasCachedProfiles() {
        List<Profile> current = profilesCache.getValue();
        return current != null && !current.isEmpty();
    }

    public void loadProfilesIfNeeded(ProfileListCallback callback) {
        if (hasCachedProfiles()) {
            callback.onSuccess(profilesCache.getValue());
            return;
        }

        apiService.getUserProfiles().enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(@NonNull Call<List<Profile>> call,
                                   @NonNull Response<List<Profile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    profilesCache.postValue(new ArrayList<>(response.body()));
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Błąd pobierania profili: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Profile>> call, @NonNull Throwable t) {
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }

    public void createProfile(Profile profile, ProfileCallback callback) {
        apiService.createProfile(profile).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call,
                                   @NonNull Response<Profile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Profile> current = new ArrayList<>(getCurrentProfiles());
                    current.add(response.body());
                    profilesCache.postValue(current);
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Błąd dodawania profilu: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }

    public void getBalance(BalanceCallback callback) {
        apiService.getBalance().enqueue(new Callback<UserBalanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserBalanceResponse> call,
                                   @NonNull Response<UserBalanceResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    callback.onSuccess(response.body().getBalance());
                else
                    callback.onError("Błąd: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<UserBalanceResponse> call,
                                  @NonNull Throwable t) {
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }

    public void updateProfile(Profile profile, ProfileCallback callback) {
        apiService.updateProfile(profile.getId(), profile).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call,
                                   @NonNull Response<Profile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Profile> current = new ArrayList<>(getCurrentProfiles());

                    for (int i = 0; i < current.size(); i++) {
                        if (current.get(i).getId().equals(response.body().getId())) {
                            current.set(i, response.body());
                            break;
                        }
                    }

                    profilesCache.postValue(current);
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Błąd aktualizacji profilu: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }

    public void deleteProfile(Long id, SimpleCallback callback) {
        apiService.deleteProfile(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    List<Profile> current = new ArrayList<>(getCurrentProfiles());
                    current.removeIf(profile -> profile.getId().equals(id));
                    profilesCache.postValue(current);
                    callback.onSuccess();
                } else {
                    callback.onError("Błąd usuwania profilu: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }

    public void deleteUser(SimpleCallback callback) {
        apiService.deleteUser().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    profilesCache.postValue(new ArrayList<>());
                    callback.onSuccess();
                } else {
                    callback.onError("Błąd usuwania twojego konta: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }

    private List<Profile> getCurrentProfiles() {
        List<Profile> current = profilesCache.getValue();
        return current != null ? current : new ArrayList<>();
    }
}