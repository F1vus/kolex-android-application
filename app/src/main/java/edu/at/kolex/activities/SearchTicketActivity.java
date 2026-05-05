package edu.at.kolex.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
            getSupportActionBar().setTitle("Szukaj biletu");
        }

        String departure = getIntent().getStringExtra("departure");
        String arrival = getIntent().getStringExtra("arrival");

        RoutesFragment fragment = new RoutesFragment();

        Bundle args = new Bundle();
        args.putString("departure", departure);
        args.putString("arrival", arrival);

        fragment.setArguments(args);

        setCurrentFragment(fragment);
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