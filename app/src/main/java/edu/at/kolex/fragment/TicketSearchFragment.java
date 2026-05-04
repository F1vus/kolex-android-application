package edu.at.kolex.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.at.kolex.activities.SearchTicketActivity;
import edu.at.kolex.databinding.FragmentTicketSearchBinding;

public class TicketSearchFragment extends Fragment {

    private FragmentTicketSearchBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTicketSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSearch.setOnClickListener(v -> {
            String departure = binding.etDeparture.getText().toString().trim();
            String arrival = binding.etArrival.getText().toString().trim();

            if (validate(departure, arrival)) {
                // 1. Create the bundle with your parameters
                Bundle bundle = new Bundle();
                bundle.putString("departure", departure);
                bundle.putString("arrival", arrival);

                startActivity(new Intent(this.getActivity(), SearchTicketActivity.class));
            }
        });
    }

    private boolean validate(String dep, String arr) {
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

        return isValid;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}