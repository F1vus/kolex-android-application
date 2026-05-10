package edu.at.kolex.repository;

import androidx.annotation.NonNull;

import java.util.List;

import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.UserApiService;
import edu.at.kolex.model.Profile;
import edu.at.kolex.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserApiService userApiService =
            ApiClient.getClient().create(UserApiService.class);
    private static UserRepository instance;

    private UserRepository() {}

    public static UserRepository getInstance() {
        if (instance == null) instance = new UserRepository();
        return instance;
    }

    public interface ProfileListCallback {
        void onSuccess(List<Profile> profiles);
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

    public interface GetUserCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public void getUserProfiles(ProfileListCallback callback) {
        userApiService.getUserProfiles().enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(@NonNull Call<List<Profile>> call,
                                   @NonNull Response<List<Profile>> response) {
                if (response.isSuccessful() && response.body() != null)
                    callback.onSuccess(response.body());
                else
                    callback.onError("Error: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<List<Profile>> call,
                                  @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void createProfile(Profile profile, ProfileCallback callback) {
        userApiService.createProfile(profile).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call,
                                   @NonNull Response<Profile> response) {
                if (response.isSuccessful() && response.body() != null)
                    callback.onSuccess(response.body());
                else
                    callback.onError("Error: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call,
                                  @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateProfile(Profile profile, ProfileCallback callback) {
        userApiService.updateProfile(profile.getId(), profile)
                .enqueue(new Callback<Profile>() {
                    @Override
                    public void onResponse(@NonNull Call<Profile> call,
                                           @NonNull Response<Profile> response) {
                        if (response.isSuccessful() && response.body() != null)
                            callback.onSuccess(response.body());
                        else
                            callback.onError("Error: " + response.code());
                    }

                    @Override
                    public void onFailure(@NonNull Call<Profile> call,
                                          @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void deleteProfile(Long id, SimpleCallback callback) {
        userApiService.deleteProfile(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.isSuccessful()) callback.onSuccess();
                else callback.onError("Error: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call,
                                  @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getUser(GetUserCallback callback) {
        userApiService.getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call,
                                   @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null)
                    callback.onSuccess(response.body());
                else
                    callback.onError("Error: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<User> call,
                                  @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}