package edu.at.kolex.repository;

import static android.provider.Settings.System.getString;

import androidx.annotation.NonNull;

import edu.at.kolex.R;
import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.ReservationApiService;
import edu.at.kolex.model.ReservationRequest;
import edu.at.kolex.model.ReservationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationRepository {
    private final ReservationApiService reservationApiService =
            ApiClient.getClient().create(ReservationApiService.class);

    private static ReservationRepository instance;

    public static ReservationRepository getInstance() {
        if (instance == null) {
            instance = new ReservationRepository();
        }
        return instance;
    }

    public interface ReservationCallback {
        void onSuccess(Long reservationId);
        void onError(String message);
    }

    public interface CancelCallback {
        void onSuccess();
        void onError(String message);
    }

    public void reserveSeat(ReservationRequest request, ReservationCallback callback) {
        reservationApiService.reserveSeat(request).enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReservationResponse> call,
                                   @NonNull Response<ReservationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getReservationId());
                } else {
                    callback.onError(getString(R.string.error) + response.code());
                }
            }

            private String getString(int error) {
                return "";
            }

            @Override
            public void onFailure(@NonNull Call<ReservationResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void cancelReservation(Long reservationId, Long profileId, CancelCallback callback) {
        reservationApiService.cancel(reservationId, profileId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(getString(R.string.error_) + response.code());
                }
            }

            private String getString(int error) {
                return "";
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}