package edu.at.kolex.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import edu.at.kolex.api.ApiClient;
import edu.at.kolex.api.TicketApiService;
import edu.at.kolex.model.Ticket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketRepository {

    private final TicketApiService ticketApiService = ApiClient.getClient().create(TicketApiService.class);
    private static TicketRepository instance;

    private static final String TAG = "TicketRepository";

    public static TicketRepository getInstance() {
        if (instance == null) {
            instance = new TicketRepository();
        }
        return instance;
    }

    public interface TicketListCallback {
        void onSuccess(List<Ticket> tickets);
        void onError(String message);
    }

    public interface TicketCallback {
        void onSuccess(Ticket ticket);
        void onError(String message);
    }

    /**
     * Fetch all tickets for the current user
     */
    public void getUserTickets(TicketListCallback callback) {
        ticketApiService.getUserTickets().enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(@NonNull Call<List<Ticket>> call, @NonNull Response<List<Ticket>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Błąd pobierania biletów: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Ticket>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching user tickets: " + t.getLocalizedMessage(), t);
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }

    /**
     * Fetch a specific ticket by ID
     */
    public void getTicketById(Long ticketId, TicketCallback callback) {
        ticketApiService.getTicketById(ticketId).enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(@NonNull Call<Ticket> call, @NonNull Response<Ticket> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Błąd pobierania biletu: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Ticket> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching ticket: " + t.getLocalizedMessage(), t);
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }

    /**
     * Fetch tickets for a specific user profile
     */
    public void getTicketsByProfile(Long profileId, TicketListCallback callback) {
        ticketApiService.getTicketsByProfile(profileId).enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(@NonNull Call<List<Ticket>> call, @NonNull Response<List<Ticket>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Błąd pobierania biletów profilu: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Ticket>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching profile tickets: " + t.getLocalizedMessage(), t);
                callback.onError("Brak połączenia z serwerem");
            }
        });
    }
}
