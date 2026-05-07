package edu.at.kolex.repository;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.UserApiService;
import edu.at.kolex.model.Profile;
import edu.at.kolex.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserApiService userApiService = ApiClient.getClient().create(UserApiService.class);
    private static UserRepository instance;
    
    private static final boolean USE_MOCK_DATA = true;
    private final List<Profile> mockProfiles = new ArrayList<>();

    private UserRepository() {
        mockProfiles.add(new Profile(1L, "Jan", "Kowalski"));
        mockProfiles.add(new Profile(2L, "Anna", "Kowalska"));
        mockProfiles.add(new Profile(3L, "Staś", "Kowalski"));
        mockProfiles.add(new Profile(4L, "Zofia", "Nowak"));
        mockProfiles.add(new Profile(5L, "Marek", "Zieliński"));
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
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

    public void getUserProfiles(ProfileListCallback callback) {
        if (USE_MOCK_DATA) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> callback.onSuccess(new ArrayList<>(mockProfiles)), 500);
            return;
        }
        userApiService.getUserProfiles().enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(@NonNull Call<List<Profile>> call, @NonNull Response<List<Profile>> response) {
                if (response.isSuccessful() && response.body() != null) callback.onSuccess(response.body());
                else callback.onError("Error: " + response.code());
            }
            @Override
            public void onFailure(@NonNull Call<List<Profile>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void createProfile(Profile profile, ProfileCallback callback) {
        if (USE_MOCK_DATA) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                long newId = mockProfiles.isEmpty() ? 1L : mockProfiles.get(mockProfiles.size() - 1).getId() + 1;
                Profile newProfile = new Profile(newId, profile.getFirstName(), profile.getLastName());
                mockProfiles.add(newProfile);
                callback.onSuccess(newProfile);
            }, 500);
            return;
        }
        userApiService.createProfile(profile).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                if (response.isSuccessful()) callback.onSuccess(response.body());
                else callback.onError("Error: " + response.code());
            }
            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateProfile(Profile profile, ProfileCallback callback) {
        if (USE_MOCK_DATA) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                for (int i = 0; i < mockProfiles.size(); i++) {
                    if (mockProfiles.get(i).getId().equals(profile.getId())) {
                        mockProfiles.set(i, profile);
                        callback.onSuccess(profile);
                        return;
                    }
                }
                callback.onError("Not found");
            }, 500);
            return;
        }
        userApiService.updateProfile(profile.getId(), profile).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                if (response.isSuccessful()) callback.onSuccess(response.body());
                else callback.onError("Error: " + response.code());
            }
            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void deleteProfile(Long id, SimpleCallback callback) {
        if (USE_MOCK_DATA) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mockProfiles.removeIf(p -> p.getId().equals(id));
                callback.onSuccess();
            }, 500);
            return;
        }
        userApiService.deleteProfile(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) callback.onSuccess();
                else callback.onError("Error: " + response.code());
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getUser(UserRepository.GetUserCallback callback) {
        userApiService.getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) callback.onSuccess(response.body());
                else callback.onError("Error: " + response.code());
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface GetUserCallback {
        void onSuccess(User user);
        void onError(String message);
    }
    
    public interface ProfileUpdateCallback extends ProfileCallback {}
}
