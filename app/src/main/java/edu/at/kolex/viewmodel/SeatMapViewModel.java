package edu.at.kolex.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.at.kolex.model.ReservationRequest;
import edu.at.kolex.model.SeatStatus;
import edu.at.kolex.repository.ReservationRepository;
import edu.at.kolex.repository.TravelRepository;

public class SeatMapViewModel extends ViewModel {

    private final TravelRepository repository = TravelRepository.getInstance();
    private final ReservationRepository reservationRepository = ReservationRepository.getInstance();

    private final MutableLiveData<List<List<SeatStatus>>> wagons = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Long> reservationId = new MutableLiveData<>();

    public LiveData<List<List<SeatStatus>>> getWagons() {
        return wagons;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Long> getReservationId() {
        return reservationId;
    }

    public void loadSeats(long travelId, int startStop, int endStop) {
        loading.setValue(true);

        repository.getSeats(travelId, startStop, endStop, new TravelRepository.SeatsCallback() {
            @Override
            public void onSuccess(List<SeatStatus> seats) {
                loading.setValue(false);
                wagons.setValue(groupIntoWagons(seats));
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                error.setValue(message);
            }
        });
    }

    public void reserveSeat(Long seatId, Long profileId, int startStopNumber, int endStopNumber) {
        loading.setValue(true);

        ReservationRequest request = new ReservationRequest();
        request.setSeatId(seatId);
        request.setProfileId(profileId);
        request.setStartStopNumber(startStopNumber);
        request.setEndStopNumber(endStopNumber);

        reservationRepository.reserveSeat(request, new ReservationRepository.ReservationCallback() {
            @Override
            public void onSuccess(Long id) {
                loading.setValue(false);
                reservationId.setValue(id);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                error.setValue(message);
            }
        });
    }

    private List<List<SeatStatus>> groupIntoWagons(List<SeatStatus> seats) {
        seats.sort(Comparator.comparingInt(SeatStatus::getSeatNumber));

        Map<Integer, List<SeatStatus>> grouped = new TreeMap<>();
        for (SeatStatus seat : seats) {
            int wagonIndex = (seat.getSeatNumber() - 1) / 20;
            grouped.computeIfAbsent(wagonIndex, k -> new ArrayList<>()).add(seat);
        }
        return new ArrayList<>(grouped.values());
    }
}