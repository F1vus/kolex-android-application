package edu.at.kolex.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import edu.at.kolex.R;
import edu.at.kolex.activities.MainActivity;
import edu.at.kolex.databinding.FragmentLoginBinding;
import edu.at.kolex.utils.ValidationUtils;
import edu.at.kolex.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {

            if (result.isSuccess()) {
                startActivity(new Intent(requireContext(), MainActivity.class));
                requireActivity().finish();
            } else {
                Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (!ValidationUtils.isValidEmail(email)) {
                binding.etEmail.setError("Incorrect email address");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                binding.etPassword.setError("Enter your password");
                return;
            }

            viewModel.login(email, password);
        });

        binding.tvRegister.setOnClickListener(v -> {
            NavController navController =
                    NavHostFragment.findNavController(LoginFragment.this);
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}