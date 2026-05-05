package edu.at.kolex.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Calendar;

import edu.at.kolex.R;
import edu.at.kolex.databinding.FragmentTicketSearchBinding;

public class TicketSearchFragment extends Fragment {

    private FragmentTicketSearchBinding binding;

    private final String[] stations = {
            "Warszawa",
            "Kraków",
            "Gdańsk",
            "Wrocław",
            "Poznań"
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTicketSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                stations
        );

        binding.etDeparture.setAdapter(adapter);
        binding.etArrival.setAdapter(adapter);

        binding.etDate.setOnClickListener(v -> showDate());

        binding.btnSearch.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("departure", binding.etDeparture.getText().toString());
            b.putString("arrival", binding.etArrival.getText().toString());

            Navigation.findNavController(v)
                    .navigate(R.id.action_searchFragment_to_routesFragment, b);
        });
    }

    private void showDate() {
        Calendar c = Calendar.getInstance();

        DatePickerDialog d = new DatePickerDialog(
                requireContext(),
                (v, y, m, d1) -> binding.etDate.setText(d1 + "." + (m + 1) + "." + y),
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );

        d.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}