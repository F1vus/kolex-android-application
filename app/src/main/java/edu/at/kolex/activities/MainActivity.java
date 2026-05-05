package edu.at.kolex.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Optional;

import edu.at.kolex.R;
import edu.at.kolex.fragment.UserFragment;
import edu.at.kolex.fragment.TicketSearchFragment;
import edu.at.kolex.fragment.TicketsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        Optional.ofNullable(actionBar).ifPresent(ActionBar::hide);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Only set initial fragment if this is a fresh launch
        if (savedInstanceState == null) {
            setCurrentFragment(new TicketSearchFragment());
            bottomNavigationView.setSelectedItemId(R.id.search);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;

            if (itemId == R.id.search) {
                selectedFragment = new TicketSearchFragment();
            } else if (itemId == R.id.profile) {
                selectedFragment = new UserFragment();
            } else if (itemId == R.id.tickets) {
                selectedFragment = new TicketsFragment();
            }

            if (selectedFragment != null) {
                setCurrentFragment(selectedFragment);
            }
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }
}
