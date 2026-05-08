package edu.at.kolex.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import edu.at.kolex.model.Station;
import edu.at.kolex.repository.TravelRepository;

public class TravelViewModel extends ViewModel {
    private final TravelRepository repository = TravelRepository.getInstance();

    private final MutableLiveData<List<Station>> stations = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<Station>> getStations() {
        return stations;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadStations() {
        repository.getStations(new TravelRepository.StationsCallback() {
            @Override
            public void onSuccess(List<Station> data) {
                stations.setValue(data);
            }

            @Override
            public void onError(String message) {
                error.setValue(message);
            }
        });
    }
}
