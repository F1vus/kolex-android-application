package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.at.kolex.databinding.FragmentEditProfileBinding;
import edu.at.kolex.model.ProfileDTO;
import edu.at.kolex.repository.UserRepository;

public class EditProfileFragment extends Fragment {

    private static final String ARG_PROFILE = "profile";
    private FragmentEditProfileBinding binding;
    private ProfileDTO profile;
    private UserRepository repository;

    public static EditProfileFragment newInstance(ProfileDTO profile) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE, profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profile = (ProfileDTO) getArguments().getSerializable(ARG_PROFILE);
        }
        repository = UserRepository.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        if (profile != null) {
            binding.etFirstName.setText(profile.getFirstName());
            binding.etLastName.setText(profile.getLastName());
        }

        binding.editToolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        binding.btnSave.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        String firstName = binding.etFirstName.getText().toString().trim();
        String lastName = binding.etLastName.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        profile.setFirstName(firstName);
        profile.setLastName(lastName);

        binding.editProgressBar.setVisibility(View.VISIBLE);
        binding.btnSave.setEnabled(false);

        repository.updateProfile(profile, new UserRepository.ProfileUpdateCallback() {
            @Override
            public void onSuccess(ProfileDTO updatedProfile) {
                if (isAdded()) {
                    binding.editProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onError(String message) {
                if (isAdded()) {
                    binding.editProgressBar.setVisibility(View.GONE);
                    binding.btnSave.setEnabled(true);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
