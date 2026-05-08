package edu.at.kolex.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.List;

import edu.at.kolex.R;
import edu.at.kolex.adapter.RouteAdapter;
import edu.at.kolex.model.Route;
import edu.at.kolex.repository.TravelRepository;

public class RoutesFragment extends Fragment {

    private RecyclerView rvRoutes;
    private ProgressBar progressBar;
    private View statusLayout;
    private ImageView ivStatusIcon;
    private TextView tvStatusTitle, tvStatusSubtitle;
    private Button btnRetry;
    private RouteAdapter adapter;
    private Long fromId, toId;
    private LocalDateTime dateAndTimeSearchTrain;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fromId = getArguments() != null ? getArguments().getLong("departure_id", 0L) : 0L;
        toId = getArguments() != null ? getArguments().getLong("arrival_id", 0L) : 0L;

        if (getArguments() != null) {
            dateAndTimeSearchTrain = (LocalDateTime) getArguments().getSerializable("date_and_time");
        } else {
            dateAndTimeSearchTrain = LocalDateTime.now();
        }


        initViews(view);
        setupRecyclerView();
        searchRoutes();
    }

    private void initViews(View view) {
        rvRoutes = view.findViewById(R.id.rvRoutes);
        progressBar = view.findViewById(R.id.progressBar);
        statusLayout = view.findViewById(R.id.statusLayout);
        ivStatusIcon = view.findViewById(R.id.ivStatusIcon);
        tvStatusTitle = view.findViewById(R.id.tvStatusTitle);
        tvStatusSubtitle = view.findViewById(R.id.tvStatusSubtitle);
        btnRetry = view.findViewById(R.id.btnRetry);
    }

    private void setupRecyclerView() {
        adapter = new RouteAdapter(route -> {
            // Handle route selection
            Toast.makeText(getContext(), "Selected: " + route.getTrainName(), Toast.LENGTH_SHORT).show();
        });
        rvRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRoutes.setAdapter(adapter);
    }

    private void searchRoutes() {
        setUiState(true, false, false);
        
        Log.v("RoutesFragment", "FromId: "+fromId+" toId: "+toId);

        TravelRepository.getInstance().searchTrains(fromId, toId, dateAndTimeSearchTrain, new TravelRepository.TravelCallback() {
            @Override
            public void onSuccess(List<Route> routes) {
                if (!isAdded()) return;
                if (routes == null || routes.isEmpty()) {
                    setUiState(false, false, true);
                    showStatus("No connections found", "Try searching for another date or station.", android.R.drawable.ic_dialog_info);
                } else {
                    setUiState(false, true, false);
                    adapter.updateRoutes(routes);
                    Log.v("RoutesFragment: ", "GET entity: "+routes.get(0).toString());
                }
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                setUiState(false, false, true);
                showStatus("Something went wrong", message, android.R.drawable.stat_notify_error);
                btnRetry.setVisibility(View.VISIBLE);
                btnRetry.setOnClickListener(v -> searchRoutes());
            }

            @Override
            public void onNoInternet() {
                if (!isAdded()) return;
                setUiState(false, false, true);
                showStatus("No Internet", "Please check your network connection and try again.", android.R.drawable.ic_dialog_alert);
                btnRetry.setVisibility(View.VISIBLE);
                btnRetry.setOnClickListener(v -> searchRoutes());
            }
        });
    }

    private void setUiState(boolean loading, boolean showList, boolean showStatus) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        rvRoutes.setVisibility(showList ? View.VISIBLE : View.GONE);
        statusLayout.setVisibility(showStatus ? View.VISIBLE : View.GONE);
        if (loading) btnRetry.setVisibility(View.GONE);
    }

    private void showStatus(String title, String subtitle, int iconRes) {
        tvStatusTitle.setText(title);
        tvStatusSubtitle.setText(subtitle);
        ivStatusIcon.setImageResource(iconRes);
    }
}
