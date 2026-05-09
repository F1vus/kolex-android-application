package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.at.kolex.R;
import edu.at.kolex.adapter.StopSegmentAdapter;
import edu.at.kolex.model.Route;
import edu.at.kolex.model.TravelStop;
import edu.at.kolex.repository.TravelRepository;

public class ConnectionDetailsFragment extends Fragment {

    private static final String ARG_ROUTE = "route";

    private Route route;

    private RecyclerView rvSegments;
    private ProgressBar progressBar;
    private View errorLayout;
    private TextView tvErrorMsg, tvTotalPrice, tvTripDate;
    private Button btnRetry, btnContinue;
    private StopSegmentAdapter adapter;

    public static ConnectionDetailsFragment newInstance(Route route) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ROUTE, route);
        ConnectionDetailsFragment f = new ConnectionDetailsFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            route = (Route) getArguments().getSerializable(ARG_ROUTE);
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

        if (route != null && route.getPrice() != null) {
            Locale pl = new Locale("pl", "PL");
            tvTotalPrice.setText(String.format(pl,
                    "Cena biletu: %.2f zł", route.getPrice()));
        }

        if (route != null) {
            LocalDateTime dep = LocalDateTime.parse(route.getActualDeparture());
            tvTripDate.setText(dep.format(
                    DateTimeFormatter.ofPattern("EEEE, d MMMM",
                            new Locale("pl", "PL"))));
        }

        adapter = new StopSegmentAdapter();
        rvSegments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSegments.setAdapter(adapter);

        btnContinue.setOnClickListener(v ->
                Toast.makeText(getContext(),
                        "Przejście do wyboru miejsca — TODO",
                        Toast.LENGTH_SHORT).show());

        loadStops();
    }

    private void loadStops() {
        if (route == null) return;
        setUiState(true, false, false);

        TravelRepository.getInstance().getStopsByTravelId(
                route.getTravelId(),
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

    /**
     * Zamienia listę przystanków na segmenty (pary sąsiednich przystanków)
     * wyłącznie między travelStopNumberFrom a travelStopNumberTo.
     */
    private List<StopSegmentAdapter.Segment> buildSegments(
            List<TravelStop> allStops) {

        List<StopSegmentAdapter.Segment> result = new ArrayList<>();
        if (allStops == null || allStops.isEmpty() || route == null) return result;

        TravelStop fromStop = null;
        for (TravelStop s : allStops) {
            if (s.getStopNumber() == route.getTravelStopNumberFrom()) {
                fromStop = s;
                break;
            }
        }
        if (fromStop == null) return result;

        LocalDateTime actualDep = LocalDateTime.parse(route.getActualDeparture());
        Duration fromOffset = Duration.parse(fromStop.getDepartureOffset());
        LocalDateTime baseDeparture = actualDep.minus(fromOffset);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        // Filtruj tylko przystanki na naszym odcinku i posortuj
        List<TravelStop> relevant = new ArrayList<>();
        for (TravelStop s : allStops) {
            if (s.getStopNumber() >= route.getTravelStopNumberFrom()
                    && s.getStopNumber() <= route.getTravelStopNumberTo()) {
                relevant.add(s);
            }
        }
        relevant.sort((a, b) -> Integer.compare(a.getStopNumber(), b.getStopNumber()));

        // Buduj segmenty z par sąsiednich przystanków
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
                    route.getTrainName() != null ? route.getTrainName() : "–",
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