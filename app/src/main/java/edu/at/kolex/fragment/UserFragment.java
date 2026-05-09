package edu.at.kolex.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.at.kolex.activities.AuthActivity;
import edu.at.kolex.activities.ProfilesActivity;
import edu.at.kolex.databinding.FragmentUserBinding;
import edu.at.kolex.model.User;
import edu.at.kolex.repository.UserRepository;
import edu.at.kolex.utils.TokenManager;

public class UserFragment extends Fragment {
    private FragmentUserBinding binding;

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

        binding.btnLogout.setOnClickListener(v ->{
            TokenManager.clearToken(requireContext());
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void loadUserData() {
        String cachedEmail = TokenManager.getEmail(requireContext());
        if (cachedEmail != null) {
            binding.tvUserEmail.setText(cachedEmail);
        }

        UserRepository.getInstance().getUser(new UserRepository.GetUserCallback() {
            @Override
            public void onSuccess(User user) {
                if (isAdded() && user != null) {
                    binding.tvUserEmail.setText(user.getEmail());
                    TokenManager.saveEmail(requireContext(), user.getEmail());
                }
            }

            @Override
            public void onError(String message) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
