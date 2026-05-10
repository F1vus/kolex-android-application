package edu.at.kolex.viewmodel;

import static android.provider.Settings.System.getString;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import edu.at.kolex.R;
import edu.at.kolex.data.TicketMockData;
import edu.at.kolex.model.Ticket;
import edu.at.kolex.repository.TicketRepository;

public class TicketsViewModel extends ViewModel {
    
    private final MutableLiveData<List<Ticket>> ticketsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final TicketRepository ticketRepository = TicketRepository.getInstance();
    
    // Flag to use mock data for development/testing
    private static final boolean USE_MOCK_DATA = true;

    /**
     * Get the live data for tickets
     */
    public LiveData<List<Ticket>> getTickets() {
        return ticketsLiveData;
    }

    /**
     * Get the live data for loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    /**
     * Get the live data for error messages
     */
    public LiveData<String> getError() {
        return errorLiveData;
    }

    /**
     * Load user tickets
     * Uses mock data if USE_MOCK_DATA is true, otherwise calls API
     */
    public void loadUserTickets() {
        if (USE_MOCK_DATA) {
            loadMockUserTickets();
        } else {
            loadUserTicketsFromAPI();
        }
    }

    /**
     * Load user tickets from API (real data)
     */
    private void loadUserTicketsFromAPI() {
        isLoadingLiveData.setValue(true);
        ticketRepository.getUserTickets(new TicketRepository.TicketListCallback() {
            @Override
            public void onSuccess(List<Ticket> tickets) {
                ticketsLiveData.setValue(tickets);
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(null);
            }

            @Override
            public void onError(String message) {
                ticketsLiveData.setValue(new ArrayList<>());
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(message);
            }
        });
    }

    /**
     * Load mock user tickets (development/testing)
     */
    private void loadMockUserTickets() {
        isLoadingLiveData.setValue(true);
        // Simulate network delay
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            try {
                List<Ticket> mockTickets = TicketMockData.getMockTickets();
                Log.d(getString(R.string.ticketsviewmodel), getString(R.string.mock_tickets_loaded) + mockTickets.size());
                ticketsLiveData.setValue(mockTickets);
                errorLiveData.setValue(null);
            } catch (Exception e) {
                ticketsLiveData.setValue(new ArrayList<>());
                errorLiveData.setValue(getString(R.string.error_loading_mock_data) + e.getMessage());
            }
            isLoadingLiveData.setValue(false);
        }, 1000); // Simulate 1 second network delay
    }

    private String getString(int ticketsviewmodel) {
        return "";
    }

    /**
     * Load tickets for a specific profile
     * Uses mock data if USE_MOCK_DATA is true, otherwise calls API
     */
    public void loadProfileTickets(Long profileId) {
        if (USE_MOCK_DATA) {
            loadMockProfileTickets(profileId);
        } else {
            loadProfileTicketsFromAPI(profileId);
        }
    }

    /**
     * Load profile tickets from API (real data)
     */
    private void loadProfileTicketsFromAPI(Long profileId) {
        isLoadingLiveData.setValue(true);
        ticketRepository.getTicketsByProfile(profileId, new TicketRepository.TicketListCallback() {
            @Override
            public void onSuccess(List<Ticket> tickets) {
                ticketsLiveData.setValue(tickets);
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(null);
            }

            @Override
            public void onError(String message) {
                ticketsLiveData.setValue(new ArrayList<>());
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(message);
            }
        });
    }

    /**
     * Load mock profile tickets (development/testing)
     */
    private void loadMockProfileTickets(Long profileId) {
        isLoadingLiveData.setValue(true);
        // Simulate network delay
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            try {
                List<Ticket> mockTickets = TicketMockData.getMockTicketsByProfile(profileId);
                ticketsLiveData.setValue(mockTickets);
                errorLiveData.setValue(null);
            } catch (Exception e) {
                ticketsLiveData.setValue(new ArrayList<>());
                errorLiveData.setValue(getString(R.string.error_loading_mock_data_) + e.getMessage());
            }
            isLoadingLiveData.setValue(false);
        }, 1000); // Simulate 1 second network delay
    }

    /**
     * Clear error message
     */
    public void clearError() {
        errorLiveData.setValue(null);
    }

    /**
     * Check if using mock data
     * @return true if using mock data, false if using real API
     */
    public static boolean isUsingMockData() {
        return USE_MOCK_DATA;
    }
}
