package edu.at.kolex.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.Objects;

import edu.at.kolex.R;
import edu.at.kolex.activities.MainActivity;
import edu.at.kolex.databinding.FragmentRegisterBinding;
import edu.at.kolex.utils.ValidationUtils;
import edu.at.kolex.viewmodel.RegisterViewModel;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getRegisterResult().observe(getViewLifecycleOwner(), result -> {

            if (result.isSuccess()) {
                startActivity(new Intent(requireContext(), MainActivity.class));
                requireActivity().finish();
            } else {
                Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = Objects.requireNonNullElse(binding.etPassword.getText(), "").toString().trim();
            String passwordConfirm =Objects.requireNonNullElse(binding.etPasswordConfirm.getText(), "").toString().trim();

            if (!ValidationUtils.isValidEmail(email)) {
                binding.etEmail.setError("Enter your email address");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                binding.etPasswordLayout.setError("Enter your password");
                return;
            }

            if (TextUtils.isEmpty(passwordConfirm)) {
                binding.etPasswordConfirmLayout.setError("Confirm your password");
                return;
            }

            if(!ValidationUtils.isMatchPassword(password, passwordConfirm)){
                binding.etPasswordLayout.setError("Passwords do not match");
                binding.etPasswordConfirmLayout.setError("Passwords do not match");
                return;
            }

            viewModel.register(email, password, passwordConfirm);
        });

        binding.tvLogin.setOnClickListener(v -> {
            NavController navController =
                    NavHostFragment.findNavController(RegisterFragment.this);

            navController.navigate(R.id.action_registerFragment_to_loginFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

