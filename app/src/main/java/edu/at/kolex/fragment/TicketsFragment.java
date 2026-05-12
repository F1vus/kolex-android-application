package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.at.kolex.R;
import edu.at.kolex.adapter.TicketAdapter;
import edu.at.kolex.databinding.FragmentTicketsBinding;
import edu.at.kolex.model.Ticket;
import edu.at.kolex.viewmodel.TicketsViewModel;

public class TicketsFragment extends Fragment implements TicketAdapter.OnTicketClickListener {
    private FragmentTicketsBinding binding;
    private TicketAdapter ticketAdapter;
    private TicketsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTicketsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViewModel();
        initializeRecyclerView();
        observeViewModel();
        loadTickets();
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(TicketsViewModel.class);
    }

    private void initializeRecyclerView() {
        ticketAdapter = new TicketAdapter(new ArrayList<>(), this);
        binding.rvTickets.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTickets.setAdapter(ticketAdapter);
    }

    private void observeViewModel() {
        viewModel.getTickets().observe(getViewLifecycleOwner(), tickets -> {
            if (tickets != null) {
                updateUI(tickets);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                // Handle error
            }
        });
    }

    private void updateUI(List<Ticket> tickets) {
        if (tickets.isEmpty()) {
            binding.rvTickets.setVisibility(View.GONE);
            binding.emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            binding.rvTickets.setVisibility(View.VISIBLE);
            binding.emptyStateLayout.setVisibility(View.GONE);
            ticketAdapter.updateTickets(tickets);
        }
    }

    private void loadTickets() {
        viewModel.loadUserTickets();
    }

    @Override
    public void onTicketClick(Ticket ticket) {
        TicketDetailsFragment fragment = TicketDetailsFragment.newInstance(ticket);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
