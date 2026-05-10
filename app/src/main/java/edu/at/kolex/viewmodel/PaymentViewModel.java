package edu.at.kolex.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.at.kolex.model.BuyRandomTicketRequest;
import edu.at.kolex.model.BuyTicketRequest;
import edu.at.kolex.repository.PaymentRepository;

public class PaymentViewModel extends ViewModel {

    private final PaymentRepository repository = PaymentRepository.getInstance();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> success = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private static final String TAG = PaymentRepository.class.getName();

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getSuccess() {
        return success;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void buyTicket(Long reservationId) {
        loading.setValue(true);

        BuyTicketRequest request = new BuyTicketRequest();
        request.setReservationId(reservationId);

        repository.buyTicket(request, new PaymentRepository.PaymentCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
                success.setValue(true);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                Log.e(TAG, message);
                error.setValue(message);
            }
        });
    }
    public void purchaseRandom(Long travelId, Long profileId, int startStop, int endStop) {
        loading.setValue(true);

        BuyRandomTicketRequest request = new BuyRandomTicketRequest();
        request.setTravelId(travelId);
        request.setProfileId(profileId);
        request.setStartStop(startStop);
        request.setEndStop(endStop);

        repository.purchaseRandom(request, new PaymentRepository.PaymentCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
                success.setValue(true);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                error.setValue(message);
            }
        });
    }

}
