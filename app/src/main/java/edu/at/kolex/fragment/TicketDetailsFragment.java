package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.at.kolex.databinding.FragmentTicketDetailsBinding;
import edu.at.kolex.model.Ticket;

public class TicketDetailsFragment extends Fragment {

    private FragmentTicketDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTicketDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Ticket ticket = (Ticket) getArguments().getSerializable("ticket");

        if (ticket != null) {
            binding.tvName.setText("Pociąg: " + ticket.getTrainNumber());
            binding.tvPrice.setText("Cena: " + ticket.getPrice());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}