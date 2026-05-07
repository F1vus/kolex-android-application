package edu.at.kolex.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.TravelApiService;
import edu.at.kolex.model.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelRepository {
    private final TravelApiService travelApiService = ApiClient.getClient().create(TravelApiService.class);
    private static TravelRepository instance;

    public static TravelRepository getInstance() {
        if (instance == null) {
            instance = new TravelRepository();
        }
        return instance;
    }

    public interface TravelCallback {
        void onSuccess(List<Route> routes);
        void onError(String message);
        void onNoInternet();
    }

    public void searchTrains(Long fromId, Long toId, LocalDateTime dateAndTimeSearchTrain, TravelCallback callback) {
        travelApiService.searchTrains(fromId, toId, dateAndTimeSearchTrain).enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(@NonNull Call<List<Route>> call, @NonNull Response<List<Route>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Route>> call, @NonNull Throwable t) {
                Log.e("TravelRepository", "Error: " + t.getMessage(), t);
                if (t instanceof java.io.IOException) {
                    callback.onNoInternet();
                } else {
                    callback.onError(t.getMessage());
                }
            }
        });
    }
}
