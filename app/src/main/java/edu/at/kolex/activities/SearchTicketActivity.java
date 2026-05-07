package edu.at.kolex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.time.LocalDateTime;

import edu.at.kolex.R;
import edu.at.kolex.fragment.RoutesFragment;

public class SearchTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ticket);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RoutesFragment fragment = getRoutesFragment();

        setCurrentFragment(fragment);
    }

    @NonNull
    private RoutesFragment getRoutesFragment() {
        Intent intent = getIntent();

        String departure = intent.getStringExtra("departure");
        String arrival = intent.getStringExtra("arrival");

        LocalDateTime dateTime =
                (LocalDateTime) intent.getSerializableExtra("date_and_time");

        Bundle bundle = new Bundle();
        bundle.putString("departure", departure);
        bundle.putString("arrival", arrival);
        bundle.putSerializable("date_and_time", dateTime);

        RoutesFragment fragment = new RoutesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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