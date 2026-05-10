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

import edu.at.kolex.R;
import edu.at.kolex.databinding.FragmentEditProfileBinding;
import edu.at.kolex.model.Profile;
import edu.at.kolex.viewmodel.ProfilesViewModel;

public class EditProfileFragment extends Fragment {

    private static final String ARG_PROFILE = "profile";

    private FragmentEditProfileBinding binding;
    private Profile profile;
    private ProfilesViewModel viewModel;

    public static EditProfileFragment newInstance(Profile profile) {
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
            profile = (Profile) getArguments().getSerializable(ARG_PROFILE);
        }

        viewModel = new ViewModelProvider(this).get(ProfilesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (profile != null) {
            binding.etFirstName.setText(profile.getFirstName());
            binding.etLastName.setText(profile.getLastName());
            binding.btnDelete.setVisibility(View.VISIBLE);
        } else {
            binding.btnDelete.setVisibility(View.GONE);
        }

        binding.editToolbar.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        binding.btnSave.setOnClickListener(v -> saveChanges());
        binding.btnDelete.setOnClickListener(v -> deleteProfile());

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.editProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnSave.setEnabled(!isLoading);
            binding.btnDelete.setEnabled(!isLoading);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void saveChanges() {
        String firstName = binding.etFirstName.getText().toString().trim();
        String lastName = binding.etLastName.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(requireContext(), R.string.Fields_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.saveProfile(profile, firstName, lastName);
    }

    private void deleteProfile() {
        if (profile == null || profile.getId() == null) return;
        viewModel.deleteProfile(profile.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}