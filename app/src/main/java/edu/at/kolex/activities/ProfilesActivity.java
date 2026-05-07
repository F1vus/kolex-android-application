package edu.at.kolex.activities;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import edu.at.kolex.R;
import edu.at.kolex.adapter.ProfileAdapter;
import edu.at.kolex.databinding.ActivityProfilesBinding;
import edu.at.kolex.fragment.EditProfileFragment;
import edu.at.kolex.model.Profile;
import edu.at.kolex.viewmodel.ProfilesViewModel;

public class ProfilesActivity extends AppCompatActivity implements ProfileAdapter.OnProfileClickListener {

    private ActivityProfilesBinding binding;
    private ProfileAdapter adapter;
    private ProfilesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecyclerView();

        viewModel = new ViewModelProvider(this).get(ProfilesViewModel.class);

        viewModel.getProfiles().observe(this, profiles -> adapter.updateProfiles(profiles));

        viewModel.getLoading().observe(this, isLoading -> binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        viewModel.loadProfiles();

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.fabAddProfile.setOnClickListener(v -> {
            // Open EditProfileFragment in "create mode" (no profile passed)
            EditProfileFragment fragment = EditProfileFragment.newInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack("create_profile")
                    .commit();
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                viewModel.loadProfiles();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new ProfileAdapter(new ArrayList<>(), this);
        binding.rvProfiles.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProfiles.setAdapter(adapter);
    }

    @Override
    public void onProfileClick(Profile profile) {
        EditProfileFragment fragment = EditProfileFragment.newInstance(profile);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack("edit_profile")
                .commit();
    }
}
