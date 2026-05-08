package edu.at.kolex.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import edu.at.kolex.activities.SearchTicketActivity;
import edu.at.kolex.databinding.FragmentTravelSearchBinding;
import edu.at.kolex.model.Station;
import edu.at.kolex.viewmodel.TravelViewModel;


public class TravelSearchFragment extends Fragment {

    private FragmentTravelSearchBinding binding;

    private LocalDateTime dateAndTimeSearchTrain;

    private TravelViewModel viewModel;

    private Station selectedDepartureStation;
    private Station selectedArrivalStation;


    private ArrayAdapter<Station> stationAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTravelSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel = new ViewModelProvider(this).get(TravelViewModel.class);

        viewModel.getStations().observe(getViewLifecycleOwner(), stations -> {
            stationAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    stations
            );

            binding.etDeparture.setAdapter(stationAdapter);
            binding.etArrival.setAdapter(stationAdapter);
        });


        viewModel.getError().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        setupStationSelection();
        viewModel.loadStations();


        binding.etDate.setOnClickListener(v -> showDatePicker());

        binding.btnSearch.setOnClickListener(v -> {
            String departure = binding.etDeparture.getText().toString().trim();
            String arrival = binding.etArrival.getText().toString().trim();

            if (validate(departure, arrival, dateAndTimeSearchTrain, selectedDepartureStation, selectedArrivalStation)) {
                Intent intent = new Intent(requireContext(), SearchTicketActivity.class);
                intent.putExtra("departure_id", selectedDepartureStation.getId());
                intent.putExtra("arrival_id", selectedArrivalStation.getId());
                intent.putExtra("date_and_time", dateAndTimeSearchTrain);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupStationSelection() {
        binding.etDeparture.setOnItemClickListener((parent, view, position, id) -> {
            selectedDepartureStation = stationAdapter.getItem(position);
            binding.etDepartureLayout.setError(null);
        });

        binding.etArrival.setOnItemClickListener((parent, view, position, id) -> {
            selectedArrivalStation = stationAdapter.getItem(position);
            binding.etArrivalLayout.setError(null);
        });
    }


    private boolean validate(String dep, String arr, LocalDateTime date,
                             Station departureStation,
                             Station arrivalStation) {
        boolean isValid = true;

        if (dep.isEmpty() || departureStation == null) {
            binding.etDepartureLayout.setError("Wybierz stację z listy");
            isValid = false;
        } else {
            binding.etDepartureLayout.setError(null);
        }

        if (arr.isEmpty() || arrivalStation == null) {
            binding.etArrivalLayout.setError("Wybierz stację z listy");
            isValid = false;
        } else {
            binding.etArrivalLayout.setError(null);
        }

        if (date == null) {
            binding.etDateLayout.setError("Enter date and time of departure");
            isValid = false;
        } else {
            binding.etDateLayout.setError(null);
        }

        if (departureStation != null && arrivalStation != null
                && departureStation.getId().equals(arrivalStation.getId())) {
            binding.etArrivalLayout.setError("Stacje nie mogą być takie same");
            isValid = false;
        }

        return isValid;
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> showTimePicker(year, month, dayOfMonth),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void showTimePicker(int year, int month, int day) {

        Calendar now = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {

                    LocalDateTime selectedDateTime = LocalDateTime.of(
                            year,
                            month + 1,
                            day,
                            hourOfDay,
                            minute
                    );

                    LocalDateTime currentDateTime = LocalDateTime.now();

                    if (selectedDateTime.isBefore(currentDateTime)) {
                        Toast.makeText(
                                requireContext(),
                                "Nie można wybrać przeszłej godziny",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    DateTimeFormatter formatter =
                            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

                    binding.etDate.setText(selectedDateTime.format(formatter));

                    this.dateAndTimeSearchTrain = selectedDateTime;

                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
    }
}