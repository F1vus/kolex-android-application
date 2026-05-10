package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import edu.at.kolex.R;
import edu.at.kolex.activities.SearchTravelActivity;
import edu.at.kolex.adapter.StopSegmentAdapter;
import edu.at.kolex.model.Travel;
import edu.at.kolex.model.TravelStop;
import edu.at.kolex.repository.TravelRepository;

public class ConnectionDetailsFragment extends Fragment {

    private static final String ARG_TRAVEL = "travel";

    private Travel travel;

    private RecyclerView rvSegments;
    private ProgressBar progressBar;
    private View errorLayout;
    private TextView tvErrorMsg, tvTotalPrice, tvTripDate;
    private Button btnRetry, btnContinue;
    private StopSegmentAdapter adapter;

    public static ConnectionDetailsFragment newInstance(Travel travel) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRAVEL, travel);
        ConnectionDetailsFragment f = new ConnectionDetailsFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            travel = (Travel) getArguments().getSerializable(ARG_TRAVEL);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connection_details,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvSegments = view.findViewById(R.id.rvSegments);
        progressBar = view.findViewById(R.id.progressBarDetails);
        errorLayout = view.findViewById(R.id.errorLayout);
        tvErrorMsg = view.findViewById(R.id.tvErrorMsg);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        tvTripDate = view.findViewById(R.id.tvTripDate);
        btnRetry = view.findViewById(R.id.btnRetryDetails);
        btnContinue = view.findViewById(R.id.btnContinue);

        if (travel != null && travel.getPrice() != null) {
            Locale pl = new Locale("pl", "PL");
            tvTotalPrice.setText(String.format(pl,
                    "Cena biletu: %.2f zł", travel.getPrice()));
        }

        if (travel != null) {
            LocalDateTime dep = LocalDateTime.parse(travel.getActualDeparture());
            tvTripDate.setText(dep.format(
                    DateTimeFormatter.ofPattern("EEEE, d MMMM",
                            new Locale("pl", "PL"))));
        }

        adapter = new StopSegmentAdapter();
        rvSegments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSegments.setAdapter(adapter);

        btnContinue.setOnClickListener(v ->{
                SeatMapFragment nextFrag= SeatMapFragment.newInstance(travel, 1L);

                if (requireActivity() instanceof SearchTravelActivity) {
                    ((SearchTravelActivity) requireActivity()).setCurrentFragment(nextFrag, true);
                }
                });
        loadStops();
    }

    private void loadStops() {
        if (travel == null) return;
        setUiState(true, false, false);

        TravelRepository.getInstance().getStopsByTravelId(
                travel.getTravelId(),
                new TravelRepository.StopsCallback() {

                    @Override
                    public void onSuccess(List<TravelStop> stops) {
                        if (!isAdded()) return;
                        List<StopSegmentAdapter.Segment> segments =
                                buildSegments(stops);
                        if (segments.isEmpty()) {
                            showError("Brak danych o przystankach");
                        } else {
                            adapter.setSegments(segments);
                            setUiState(false, true, false);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        if (!isAdded()) return;
                        showError(message);
                        btnRetry.setVisibility(View.VISIBLE);
                        btnRetry.setOnClickListener(v -> loadStops());
                    }

                    @Override
                    public void onNoInternet() {
                        if (!isAdded()) return;
                        showError("Brak połączenia z internetem");
                        btnRetry.setVisibility(View.VISIBLE);
                        btnRetry.setOnClickListener(v -> loadStops());
                    }
                });
    }

    private List<StopSegmentAdapter.Segment> buildSegments(
            List<TravelStop> allStops) {

        List<StopSegmentAdapter.Segment> result = new ArrayList<>();
        if (allStops == null || allStops.isEmpty() || travel == null) return result;

        TravelStop fromStop = null;
        for (TravelStop s : allStops) {
            if (s.getStopNumber() == travel.getTravelStopNumberFrom()) {
                fromStop = s;
                break;
            }
        }
        if (fromStop == null) return result;

        LocalDateTime actualDep = LocalDateTime.parse(travel.getActualDeparture());
        Duration fromOffset = Duration.parse(fromStop.getDepartureOffset());
        LocalDateTime baseDeparture = actualDep.minus(fromOffset);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        List<TravelStop> relevant = new ArrayList<>();
        for (TravelStop s : allStops) {
            if (s.getStopNumber() >= travel.getTravelStopNumberFrom()
                    && s.getStopNumber() <= travel.getTravelStopNumberTo()) {
                relevant.add(s);
            }
        }
        relevant.sort(Comparator.comparingInt(TravelStop::getStopNumber));

        for (int i = 0; i < relevant.size() - 1; i++) {
            TravelStop dep = relevant.get(i);
            TravelStop arr = relevant.get(i + 1);

            LocalDateTime depTime = baseDeparture
                    .plus(Duration.parse(dep.getDepartureOffset()));
            LocalDateTime arrTime = baseDeparture
                    .plus(Duration.parse(arr.getArrivalOffset()));

            int distDiff = arr.getDistance() - dep.getDistance();

            result.add(new StopSegmentAdapter.Segment(
                    depTime.format(fmt),
                    arrTime.format(fmt),
                    dep.getStationName(),
                    arr.getStationName(),
                    travel.getTrainName() != null ? travel.getTrainName() : "–",
                    distDiff
            ));
        }
        return result;
    }

    private void setUiState(boolean loading, boolean showList, boolean showErr) {
        progressBar.setVisibility(loading   ? View.VISIBLE : View.GONE);
        rvSegments.setVisibility(showList   ? View.VISIBLE : View.GONE);
        errorLayout.setVisibility(showErr   ? View.VISIBLE : View.GONE);
        if (loading) btnRetry.setVisibility(View.GONE);
    }

    private void showError(String msg) {
        setUiState(false, false, true);
        tvErrorMsg.setText(msg);
    }
}