package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import edu.at.kolex.R;
import edu.at.kolex.activities.SearchTravelActivity;
import edu.at.kolex.adapter.WagonsPagerAdapter;
import edu.at.kolex.databinding.FragmentSeatMapBinding;
import edu.at.kolex.model.BuyRandomTicketRequest;
import edu.at.kolex.model.SeatStatus;
import edu.at.kolex.model.Travel;
import edu.at.kolex.viewmodel.SeatMapViewModel;

public class SeatMapFragment extends Fragment {

    private static final String ARG_TRAVEL_OBJECT = "travel_object";
    private static final String ARG_PROFILE_ID = "profile_id";

    private FragmentSeatMapBinding binding;
    private SeatMapViewModel viewModel;
    private WagonsPagerAdapter pagerAdapter;

    private SeatStatus selectedSeat;
    private boolean seatsLoaded = false;
    private Long profileId, userId;
    private Travel travel;

    public static SeatMapFragment newInstance(Travel travel, Long profileId) {
        SeatMapFragment fragment = new SeatMapFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRAVEL_OBJECT, travel);
        args.putLong(ARG_PROFILE_ID, profileId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSeatMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        travel = (Travel) requireArguments().getSerializable(ARG_TRAVEL_OBJECT);
        if(travel == null) return;

        profileId = requireArguments().getLong(ARG_PROFILE_ID);
        userId = 2L;

        viewModel = new ViewModelProvider(this).get(SeatMapViewModel.class);

        setupPager();
        observeViewModel();
        setupToggleMode(travel.getTravelId(), travel.getTravelStopNumberFrom(), travel.getTravelStopNumberTo());
        binding.toggleGroup.check(R.id.btnGraphical);
//        showRandomMode();

        binding.btnConfirm.setOnClickListener(v -> {
            int checkedId = binding.toggleGroup.getCheckedButtonId();

            if (checkedId == R.id.btnGraphical) {
                if (selectedSeat == null) {
                    Toast.makeText(requireContext(), "Wybierz miejsce", Toast.LENGTH_SHORT).show();
                    return;
                }

                viewModel.reserveSeat(
                        selectedSeat.getSeatId(),
                        profileId,
                        travel.getTravelStopNumberFrom(),
                        travel.getTravelStopNumberTo()
                );

            } else if (checkedId == R.id.btnRandom) {
                navigateToPaymentRandom();
            }
        });


    }

    private void navigateToPaymentRandom() {
        BuyRandomTicketRequest buyRandomTicketRequest = new BuyRandomTicketRequest();
        buyRandomTicketRequest.setProfileId(profileId);
        buyRandomTicketRequest.setUserId(userId);
        buyRandomTicketRequest.setStartStop(travel.getTravelStopNumberFrom());
        buyRandomTicketRequest.setEndStop(travel.getTravelStopNumberTo());
        buyRandomTicketRequest.setTravelId(travel.getTravelId());


        PaymentFragment fragment = PaymentFragment.newRandomInstance(
                buyRandomTicketRequest,
                "24,78 zł",
                travel.getPrice().toString()
        );


        if (requireActivity() instanceof SearchTravelActivity) {
            ((SearchTravelActivity) requireActivity()).setCurrentFragment(fragment, true);
        }
    }

    private void setupToggleMode(long travelId, int startStop, int endStop) {
        binding.toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.btnGraphical) {
                showGraphicalMode(travelId, startStop, endStop);
            } else if (checkedId == R.id.btnRandom) {
                showRandomMode();
            }
        });
    }

    private void setupPager() {
        pagerAdapter = new WagonsPagerAdapter(new ArrayList<>(), seat -> {
            selectedSeat = seat;
            binding.tvSelectedSeat.setText("Wybrane miejsce: " + seat.getSeatNumber());
            binding.btnConfirm.setEnabled(true);
            pagerAdapter.setSelectedSeat(seat.getSeatNumber());
        });

        binding.vpWagons.setAdapter(pagerAdapter);
        binding.vpWagons.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }

    private void observeViewModel() {
        viewModel.getWagons().observe(getViewLifecycleOwner(), data -> {
            pagerAdapter.setData(data);
            if (!data.isEmpty()) {
                binding.vpWagons.setCurrentItem(0, false);
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnConfirm.setEnabled(!isLoading);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getReservationId().observe(getViewLifecycleOwner(), id -> {
            if (id == null) return;

            SearchTravelActivity activity = (SearchTravelActivity) requireActivity();
            activity.onReservationSuccess(id, profileId);

            Toast.makeText(requireContext(), "Miejsce zarezerwowane", Toast.LENGTH_SHORT).show();

            PaymentFragment fragment = PaymentFragment.newInstance(
                    id,
                    userId,
                    "24,78 zł",
                    travel.getPrice().toString()
            );

            // tutaj możesz przejść do paymentFragment
            if (requireActivity() instanceof SearchTravelActivity) {
                ((SearchTravelActivity) requireActivity()).setCurrentFragment(fragment, true);
            }
        });
    }

    private void showRandomMode() {
        selectedSeat = null;
        binding.vpWagons.setVisibility(View.GONE);
        binding.tvSelectedSeat.setText("Tryb losowy aktywny");
        binding.btnConfirm.setEnabled(true);
    }

    private void showGraphicalMode(long travelId, int startStop, int endStop) {
        binding.vpWagons.setVisibility(View.VISIBLE);
        binding.tvSelectedSeat.setText("Wybierz miejsce na mapie");
        binding.btnConfirm.setEnabled(false);

        if (!seatsLoaded) {
            seatsLoaded = true;
            viewModel.loadSeats(travelId, startStop, endStop);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}