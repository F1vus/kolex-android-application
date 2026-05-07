package edu.at.kolex.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import edu.at.kolex.activities.SearchTicketActivity;
import edu.at.kolex.databinding.FragmentTicketSearchBinding;

public class TicketSearchFragment extends Fragment {

    private FragmentTicketSearchBinding binding;

    private LocalDateTime dateAndTimeSearchTrain;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTicketSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.etDate.setOnClickListener(v -> showDatePicker());

        binding.btnSearch.setOnClickListener(v -> {
            String departure = binding.etDeparture.getText().toString().trim();
            String arrival = binding.etArrival.getText().toString().trim();

            if (validate(departure, arrival, this.dateAndTimeSearchTrain)) {
                Intent intent = new Intent(requireContext(), SearchTicketActivity.class);
                intent.putExtra("departure", departure);
                intent.putExtra("arrival", arrival);
                intent.putExtra("date_and_time", this.dateAndTimeSearchTrain);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean validate(String dep, String arr, LocalDateTime date) {
        boolean isValid = true;

        if (dep.isEmpty()) {
            binding.etDepartureLayout.setError("Enter departure station");
            isValid = false;
        } else {
            binding.etDepartureLayout.setError(null);
        }

        if (arr.isEmpty()) {
            binding.etArrivalLayout.setError("Enter arrival station");
            isValid = false;
        } else {
            binding.etArrivalLayout.setError(null);
        }

        if (date == null) {
            binding.etDate.setError("Enter date and time of departure");
            isValid = false;
        } else {
            binding.etDateLayout.setError(null);
        }

        return isValid;
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    showTimePicker(year, month, dayOfMonth);
                },
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