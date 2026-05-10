package edu.at.kolex.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.TravelApiService;
import edu.at.kolex.model.SeatStatus;
import edu.at.kolex.model.Travel;
import edu.at.kolex.model.Station;
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
        void onSuccess(List<Travel> travels);
        void onError(String message);
        void onNoInternet();
    }

    public interface StationsCallback {
        void onSuccess(List<Station> stations);
        void onError(String message);
    }

    public interface SeatsCallback {
        void onSuccess(List<SeatStatus> seats);
        void onError(String message);
    }



    public void searchTravel(Long fromId, Long toId, LocalDateTime dateAndTimeSearchTrain, TravelCallback callback) {
        travelApiService.searchTravel(fromId, toId, dateAndTimeSearchTrain).enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(@NonNull Call<List<Travel>> call, @NonNull Response<List<Travel>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Travel>> call, @NonNull Throwable t) {
                Log.e("TravelRepository", "Error: " + t.getMessage(), t);
                if (t instanceof java.io.IOException) {
                    callback.onNoInternet();
                } else {
                    callback.onError(t.getMessage());
                }
            }
        });
    }

    public void getStations(StationsCallback callback) {
        travelApiService.getStations().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(@NonNull Call<List<Station>> call,
                                   @NonNull Response<List<Station>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Station>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getSeats(long travelId, int startStop, int endStop, SeatsCallback callback) {
        travelApiService.getSeats(travelId, startStop, endStop).enqueue(new Callback<List<SeatStatus>>() {
            @Override
            public void onResponse(@NonNull Call<List<SeatStatus>> call,
                                   @NonNull Response<List<SeatStatus>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SeatStatus>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
