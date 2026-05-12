package edu.at.kolex.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.util.Locale;

import edu.at.kolex.R;
import edu.at.kolex.activities.AuthActivity;
import edu.at.kolex.activities.ProfilesActivity;
import edu.at.kolex.databinding.FragmentUserBinding;
import edu.at.kolex.model.TopUpResponse;
import edu.at.kolex.repository.PaymentRepository;
import edu.at.kolex.repository.UserRepository;
import edu.at.kolex.utils.TokenManager;

public class UserFragment extends Fragment {
    private FragmentUserBinding binding;

    private static final BigDecimal TOP_UP_AMOUNT = new BigDecimal("100.00");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUserData();

        binding.btnChangePersonalData.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProfilesActivity.class);
            startActivity(intent);
        });

        binding.btnLogout.setOnClickListener(v ->exitFromApplication());
        binding.btnDeleteAccount.setOnClickListener(v -> deleteUser());

        binding.btnTopUp.setOnClickListener(v -> topUp());
    }

    private void exitFromApplication(){
        TokenManager.clearToken(requireContext());
        Intent intent = new Intent(requireContext(), AuthActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void topUp() {
        binding.btnTopUp.setEnabled(false);

        PaymentRepository.getInstance().topUp(TOP_UP_AMOUNT,
                new PaymentRepository.TopUpCallback() {
                    @Override
                    public void onSuccess(TopUpResponse response) {
                        if (!isAdded()) return;
                        binding.btnTopUp.setEnabled(true);

                        Bundle args = new Bundle();
                        args.putSerializable("amount", TOP_UP_AMOUNT);
                        args.putSerializable("new_balance", response.getNewBalance());

                        TopUpSuccessFragment fragment = new TopUpSuccessFragment();
                        fragment.setArguments(args);

                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.nav_host_fragment, fragment)
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onError(String message) {
                        if (!isAdded()) return;
                        binding.btnTopUp.setEnabled(true);
                        Toast.makeText(requireContext(), message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadUserData() {
        String cachedEmail = TokenManager.getEmail(getActivity().getApplicationContext());
        if (cachedEmail != null) {
            binding.tvUserEmail.setText(cachedEmail);
        }

        UserRepository.getInstance().getBalance(new UserRepository.BalanceCallback() {
            @Override
            public void onSuccess(BigDecimal balance) {
                if (!isAdded()) return;
                binding.tvBalance.setText(String.format(
                        new Locale("pl", "PL"), "%.2f zł", balance));
            }

            @Override
            public void onError(String message) {}
        });
    }

    private void deleteUser(){
        UserRepository.getInstance().deleteUser(new UserRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
               Toast.makeText(requireContext(), "Konto zostało usunięte!", Toast.LENGTH_LONG).show();
                exitFromApplication();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(requireContext(), "Wystąpił problem podczas usuwania twojego konta!", Toast.LENGTH_LONG).show();
            }
        });
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
