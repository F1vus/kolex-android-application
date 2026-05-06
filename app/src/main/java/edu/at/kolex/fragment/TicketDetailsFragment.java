package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.at.kolex.R;

public class TicketDetailsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticket_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String trainNumber = getArguments().getString("trainNumber", "");
        String arrivalTime = getArguments().getString("arrivalTime", "");
        String price = getArguments().getString("price", "");
        String duration = getArguments().getString("duration", "");

        TextView info = view.findViewById(R.id.ticketInfo);
        Button nextBtn = view.findViewById(R.id.btnNext);

        info.setText(
                "Pociąg: " + trainNumber + "\n" +
                        "Czas przyjazdu: " + arrivalTime + "\n" +
                        "Czas podróży: " + duration + "\n" +
                        "Cena: " + price
        );

        nextBtn.setOnClickListener(v -> {

            PaymentFragment fragment = new PaymentFragment();

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}