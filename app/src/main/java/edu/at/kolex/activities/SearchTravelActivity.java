package edu.at.kolex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.at.kolex.R;
import edu.at.kolex.fragment.TravelsFragment;
import edu.at.kolex.repository.ReservationRepository;

public class SearchTravelActivity extends AppCompatActivity {

    private static final long TIMER_DURATION_MS = TimeUnit.MINUTES.toMillis(10);

    private TextView tvReservationTimer;

    private CountDownTimer countDownTimer;

    private long remainingMs = TIMER_DURATION_MS;

    private Long reservationId = null;
    private Long profileId = null;

    private boolean protectedScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_travel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        tvReservationTimer = findViewById(R.id.tvReservationTimer);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupBackPressHandler();

        if (savedInstanceState == null) {
            setCurrentFragment(getRoutesFragment(), false);
        }
    }

    public void onReservationSuccess(Long reservationId, Long profileId) {

        this.reservationId = reservationId;
        this.profileId = profileId;

        protectedScreen = true;

        tvReservationTimer.setVisibility(View.VISIBLE);

        startReservationTimer();
    }

    private void startReservationTimer() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        remainingMs = TIMER_DURATION_MS;

        updateTimerText(remainingMs);

        countDownTimer = new CountDownTimer(remainingMs, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                remainingMs = millisUntilFinished;
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                remainingMs = 0;
                updateTimerText(0);
                cancelReservationAndGoHome();
            }

        }.start();
    }

    private void updateTimerText(long millis) {

        long totalSeconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        tvReservationTimer.setText(
                String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        );
    }

    private void setupBackPressHandler() {

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {

                    @Override
                    public void handleOnBackPressed() {
                        handleNavigationBack();
                    }
                }
        );
    }

    private void handleNavigationBack() {

        if (protectedScreen) {
            showExitDialog();
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }

    private void showExitDialog() {

        new MaterialAlertDialogBuilder(this)
                .setTitle("Opuszczenie ekranu")
                .setMessage("Czy chcesz anulować rezerwację i wyjść?")
                .setNegativeButton(
                        "Kontynuuj",
                        (dialog, which) -> dialog.dismiss()
                )
                .setPositiveButton(
                        "Anuluj rezerwację",
                        (dialog, which) -> cancelReservationAndGoHome()
                )
                .show();
    }

    private void cancelReservationAndGoHome() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (reservationId == null || profileId == null) {
            goHome();
            return;
        }

        ReservationRepository.getInstance().cancelReservation(
                reservationId,
                profileId,
                new ReservationRepository.CancelCallback() {

                    @Override
                    public void onSuccess() {
                        goHome();
                    }

                    @Override
                    public void onError(String message) {

                        Toast.makeText(
                                SearchTravelActivity.this,
                                message,
                                Toast.LENGTH_SHORT
                        ).show();

                        goHome();
                    }
                }
        );
    }

    private void goHome() {

        protectedScreen = false;

        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK
        );

        startActivity(intent);

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        handleNavigationBack();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            handleNavigationBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        super.onDestroy();
    }

    public void setCurrentFragment(Fragment fragment, boolean addToBackStack) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    @NonNull
    private TravelsFragment getRoutesFragment() {

        Intent intent = getIntent();

        long departure = intent.getLongExtra("departure_id", 0L);
        long arrival = intent.getLongExtra("arrival_id", 0L);

        LocalDateTime dateTime =
                (LocalDateTime) intent.getSerializableExtra("date_and_time");

        Bundle bundle = new Bundle();

        bundle.putLong("departure_id", departure);
        bundle.putLong("arrival_id", arrival);
        bundle.putSerializable("date_and_time", dateTime);

        TravelsFragment fragment = new TravelsFragment();

        fragment.setArguments(bundle);

        return fragment;
    }
}
