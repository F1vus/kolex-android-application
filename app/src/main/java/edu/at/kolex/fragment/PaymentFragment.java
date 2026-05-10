package edu.at.kolex.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.at.kolex.activities.MainActivity;
import edu.at.kolex.databinding.FragmentPaymentBinding;
import edu.at.kolex.model.BuyRandomTicketRequest;
import edu.at.kolex.viewmodel.PaymentViewModel;


public class PaymentFragment extends Fragment {

    private static final String ARG_RESERVATION_ID = "reservation_id";
    private static final String ARG_BALANCE = "balance";
    private static final String ARG_TICKET_PRICE = "ticket_price";

    private FragmentPaymentBinding binding;
    private PaymentViewModel viewModel;
    private Long reservationId;
    private boolean randomMode = false;

    public static PaymentFragment newInstance(Long reservationId, String balance, String ticketPrice) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RESERVATION_ID, reservationId);
        args.putString(ARG_BALANCE, balance);
        args.putString(ARG_TICKET_PRICE, ticketPrice);
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentFragment newRandomInstance(BuyRandomTicketRequest ticket, String balance, String ticketPrice) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putBoolean("random_mode", true);

        args.putLong("travel_id", ticket.getTravelId());
        args.putLong("profile_id", ticket.getProfileId());
        args.putInt("start_stop", ticket.getStartStop());
        args.putInt("end_stop", ticket.getEndStop());
        args.putString(ARG_TICKET_PRICE, ticketPrice);
        args.putString(ARG_BALANCE, balance);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = requireArguments();
        randomMode = args.getBoolean("random_mode", false);

        if (!randomMode) {
            reservationId = args.getLong(ARG_RESERVATION_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);

        Bundle args = requireArguments();
        binding.tvBalance.setText(args.getString(ARG_BALANCE, "0,00 zł"));

        if (randomMode) {

            binding.tvTicketPrice.setText(args.getString(ARG_TICKET_PRICE, "0,00 zł"));

            binding.btnPay.setOnClickListener(v -> viewModel.purchaseRandom(
                    args.getLong("travel_id"),
                    args.getLong("profile_id"),
                    args.getInt("start_stop"),
                    args.getInt("end_stop")
            ));
            binding.btnGoHome.setOnClickListener(v -> goHome());

            observeViewModel();

            return;
        }

        binding.tvTicketPrice.setText(args.getString(ARG_TICKET_PRICE, "0,00 zł"));

        binding.btnPay.setOnClickListener(v -> viewModel.buyTicket(reservationId));
        binding.btnGoHome.setOnClickListener(v -> goHome());

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnPay.setEnabled(!isLoading);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                showErrorState(message);
            }
        });

        viewModel.getSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                showSuccessState();
            }
        });
    }

    private void showErrorState(String errorMessage) {
        binding.cardPayment.setVisibility(View.GONE);
        binding.cardFailure.setVisibility(View.VISIBLE);
        binding.btnGoHome.setVisibility(View.VISIBLE);
        binding.errorMessage.setText(errorMessage);
    }
    private void showSuccessState() {
        binding.cardPayment.setVisibility(View.GONE);
        binding.cardSuccess.setVisibility(View.VISIBLE);
        binding.btnGoHome.setVisibility(View.VISIBLE);
    }

    private void goHome() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}