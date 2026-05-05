package edu.at.kolex.repository;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.UserApiService;
import edu.at.kolex.model.ProfileDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserApiService userApiService = ApiClient.getClient().create(UserApiService.class);
    private static UserRepository instance;
    
    // Flag to use mock data for development
    private static final boolean USE_MOCK_DATA = true;
    private final List<ProfileDTO> mockProfiles = new ArrayList<>();

    private UserRepository() {
        // Initialize mock data: Family & Friends
        mockProfiles.add(new ProfileDTO(1L, "Jan", "Kowalski"));
        mockProfiles.add(new ProfileDTO(2L, "Anna", "Kowalska"));
        mockProfiles.add(new ProfileDTO(3L, "Staś", "Kowalski"));
        mockProfiles.add(new ProfileDTO(4L, "Zofia", "Nowak"));
        mockProfiles.add(new ProfileDTO(5L, "Marek", "Zieliński"));
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public interface ProfileListCallback {
        void onSuccess(List<ProfileDTO> profiles);
        void onError(String message);
    }

    public interface ProfileUpdateCallback {
        void onSuccess(ProfileDTO profile);
        void onError(String message);
    }

    public void getUserProfiles(ProfileListCallback callback) {
        if (USE_MOCK_DATA) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> callback.onSuccess(new ArrayList<>(mockProfiles)), 500);
            return;
        }

        userApiService.getUserProfiles().enqueue(new Callback<List<ProfileDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProfileDTO>> call, @NonNull Response<List<ProfileDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch profiles: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProfileDTO>> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void updateProfile(ProfileDTO profile, ProfileUpdateCallback callback) {
        if (USE_MOCK_DATA) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                for (int i = 0; i < mockProfiles.size(); i++) {
                    if (mockProfiles.get(i).getId().equals(profile.getId())) {
                        mockProfiles.set(i, profile);
                        callback.onSuccess(profile);
                        return;
                    }
                }
                callback.onError("Profile not found");
            }, 500);
            return;
        }

        userApiService.updateProfile(profile.getId(), profile).enqueue(new Callback<ProfileDTO>() {
            @Override
            public void onResponse(@NonNull Call<ProfileDTO> call, @NonNull Response<ProfileDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to update profile: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileDTO> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
