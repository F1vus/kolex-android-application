package edu.at.kolex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.time.LocalDateTime;
import java.util.Objects;

import edu.at.kolex.R;
import edu.at.kolex.fragment.TravelsFragment;

public class SearchTravelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_travel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        String departureName = Objects.requireNonNull(getIntent().getStringExtra("departure_name")).split("-")[1].trim();
        String arrivalName   = Objects.requireNonNull(getIntent().getStringExtra("arrival_name")).split("-")[1].trim();
        TravelsFragment fragment = getRoutesFragment();

        TextView tvDep = toolbar.findViewById(R.id.tvToolbarDeparture);
        TextView tvArr = toolbar.findViewById(R.id.tvToolbarArrival);
        tvDep.setText(departureName);
        tvArr.setText(arrivalName);

        setCurrentFragment(getRoutesFragment());
    }

    @NonNull
    private TravelsFragment getRoutesFragment() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putLong("departure_id", intent.getLongExtra("departure_id", 0L));
        bundle.putLong("arrival_id",   intent.getLongExtra("arrival_id",   0L));
        bundle.putSerializable("date_and_time",
                (LocalDateTime) intent.getSerializableExtra("date_and_time"));

        TravelsFragment fragment = new TravelsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }
}