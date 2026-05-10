package edu.at.kolex.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import java.math.BigDecimal;

import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.PaymentApiService;
import edu.at.kolex.model.BuyRandomTicketRequest;
import edu.at.kolex.model.BuyTicketRequest;
import edu.at.kolex.model.BuyTicketResponse;
import edu.at.kolex.model.TopUpRequest;
import edu.at.kolex.model.TopUpResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {

    private final PaymentApiService apiService = ApiClient.getClient().create(PaymentApiService.class);

    private static PaymentRepository instance;

    public static PaymentRepository getInstance() {
        if (instance == null) {
            instance = new PaymentRepository();
        }
        return instance;
    }

    public interface TopUpCallback {
        void onSuccess(TopUpResponse response);
        void onError(String message);
    }

    public interface PaymentCallback {
        void onSuccess();
        void onError(String message);
    }

    public void buyTicket(BuyTicketRequest request, PaymentCallback callback) {
        apiService.buyTicket(request).enqueue(new Callback<BuyTicketResponse>() {

            @Override
            public void onResponse(@NonNull Call<BuyTicketResponse> call,
                                   @NonNull Response<BuyTicketResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    callback.onSuccess();

                } else {
                    callback.onError(getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BuyTicketResponse> call,
                                  @NonNull Throwable t) {

                callback.onError(t.getMessage());
            }
        });
    }

    public void purchaseRandom(BuyRandomTicketRequest request, PaymentCallback callback) {
        apiService.purchaseRandom(request).enqueue(new Callback<BuyTicketResponse>() {
            @Override
            public void onResponse(@NonNull Call<BuyTicketResponse> call,
                                   @NonNull Response<BuyTicketResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess();
                } else {
                    callback.onError(getErrorMessage(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BuyTicketResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void topUp(BigDecimal amount, TopUpCallback callback) {
        apiService.topUp(new TopUpRequest(amount)).enqueue(new Callback<TopUpResponse>() {
            @Override
            public void onResponse(@NonNull Call<TopUpResponse> call,
                                   @NonNull Response<TopUpResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    callback.onSuccess(response.body());
                else
                    callback.onError("Błąd: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<TopUpResponse> call,
                                  @NonNull Throwable t) {
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }

    private String getErrorMessage(Response<BuyTicketResponse> response) {
        String errorMessage = "Unknown error";

        try {
            if (response.errorBody() != null) {
                errorMessage = response.errorBody().string();
            }
        } catch (Exception e) {
            Log.e("PaymentRepository", e.getMessage(), e);
        }

        Log.e("PaymentRepository", errorMessage);
        return errorMessage;
    }
}
