package edu.at.kolex.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import edu.at.kolex.R;
import edu.at.kolex.fragment.ProfileFragment;
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
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment ticketSearchFragment = new TicketSearchFragment();
        Fragment ticketsFragment = new TicketsFragment();
        Fragment profileFragment = new ProfileFragment();

        setCurrentFragment(ticketSearchFragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.search) {
                setCurrentFragment(ticketSearchFragment);
            } else if (itemId == R.id.profile) {
                setCurrentFragment(profileFragment);
            } else if (itemId == R.id.tickets) {
                setCurrentFragment(ticketsFragment);
            }

            return true;
        });
    }

//        BottomNavigationView bottomNav =
//                findViewById(R.id.bottomNavigationView);
//
//        NavHostFragment navHostFragment =
//                (NavHostFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.nav_host_fragment);
//
//        NavController navController =
//                navHostFragment.getNavController();
//
//        NavigationUI.setupWithNavController(
//                bottomNav,
//                navController
//        );
//    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }
}