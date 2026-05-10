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
import edu.at.kolex.activities.SearchTravelActivity;
import edu.at.kolex.adapter.TravelAdapter;
import edu.at.kolex.model.Travel;
import edu.at.kolex.repository.TravelRepository;

public class TravelsFragment extends Fragment {

    private RecyclerView rvRoutes;
    private ProgressBar progressBar;
    private View statusLayout;
    private ImageView ivStatusIcon;
    private TextView tvStatusTitle, tvStatusSubtitle;
    private Button btnRetry;
    private TravelAdapter adapter;
    private Long fromId, toId;
    private LocalDateTime dateAndTimeSearchTrain;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_travels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fromId = getArguments() != null ? getArguments().getLong(getString(R.string.departure_id___), 0L) : 0L;
        toId = getArguments() != null ? getArguments().getLong(getString(R.string.arrival_id___), 0L) : 0L;

        if (getArguments() != null) {
            dateAndTimeSearchTrain = (LocalDateTime) getArguments().getSerializable(getString(R.string.date_and_time__));
        } else {
            dateAndTimeSearchTrain = LocalDateTime.now();
        }


        initViews(view);
        setupRecyclerView();
        searchTravels();
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
        adapter = new TravelAdapter(travel -> {
            Toast.makeText(getContext(), getString(R.string.selected) + travel.getTrainName(), Toast.LENGTH_SHORT).show();
            ConnectionDetailsFragment detailsFragment =
                    ConnectionDetailsFragment.newInstance(travel);

            if (requireActivity() instanceof SearchTravelActivity) {
                ((SearchTravelActivity) requireActivity()).setCurrentFragment(detailsFragment, true);
            }
        });
        rvRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRoutes.setAdapter(adapter);
    }

    private void searchTravels() {
        setUiState(true, false, false);
        
        Log.v(getString(R.string.routesfragment), getString(R.string.fromid)+fromId+getString(R.string.toid)+toId);

        TravelRepository.getInstance().searchTravel(fromId, toId, dateAndTimeSearchTrain, new TravelRepository.TravelCallback() {
            @Override
            public void onSuccess(List<Travel> travels) {
                if (!isAdded()) return;
                if (travels == null || travels.isEmpty()) {
                    setUiState(false, false, true);
                    showStatus(getString(R.string.no_connections_found), getString(R.string.try_searching_for_another_date_or_station), android.R.drawable.ic_dialog_info);
                } else {
                    setUiState(false, true, false);
                    adapter.updateRoutes(travels);
                    Log.v(getString(R.string.routesfragment_), getString(R.string.get_entity)+ travels.get(0).toString());
                }
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                setUiState(false, false, true);
                showStatus(getString(R.string.something_went_wrong), message, android.R.drawable.stat_notify_error);
                btnRetry.setVisibility(View.VISIBLE);
                btnRetry.setOnClickListener(v -> searchTravels());
            }

            @Override
            public void onNoInternet() {
                if (!isAdded()) return;
                setUiState(false, false, true);
                showStatus(getString(R.string.no_internet), getString(R.string.please_check_your_network_connection_and_try_again), android.R.drawable.ic_dialog_alert);
                btnRetry.setVisibility(View.VISIBLE);
                btnRetry.setOnClickListener(v -> searchTravels());
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
