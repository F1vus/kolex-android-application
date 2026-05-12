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
import androidx.lifecycle.ViewModelProvider;
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
import edu.at.kolex.adapter.ProfileSelectAdapter;
import edu.at.kolex.adapter.StopSegmentAdapter;
import edu.at.kolex.model.Profile;
import edu.at.kolex.model.Ticket;
import edu.at.kolex.model.Travel;
import edu.at.kolex.model.TravelStop;
import edu.at.kolex.repository.TravelRepository;
import edu.at.kolex.viewmodel.ProfilesViewModel;

public class ConnectionDetailsFragment extends Fragment {

    private static final String ARG_TRAVEL = "travel";
    private static final String ARG_TICKET = "ticket";

    private Travel travel;
    private Ticket ticket;
    private boolean isViewMode = false;

    private RecyclerView rvSegments;
    private ProgressBar progressBar;
    private View errorLayout;
    private TextView tvErrorMsg, tvTotalPrice, tvTripDate;
    private Button btnRetry, btnContinue;
    private View profileWrapper, bottomPanel;
    private StopSegmentAdapter adapter;

    private RecyclerView rvProfiles;
    private ProfileSelectAdapter profileAdapter;
    private Long selectedProfileId;
    private ProfilesViewModel profilesViewModel;

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
            ticket = (Ticket) getArguments().getSerializable(ARG_TICKET);
            isViewMode = ticket != null;
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
        rvProfiles = view.findViewById(R.id.rvProfiles);
        profileWrapper = view.findViewById(R.id.profileSelectionWrapper);
        bottomPanel = view.findViewById(R.id.bottomPanel);

        rvSegments.setNestedScrollingEnabled(false);
        rvProfiles.setNestedScrollingEnabled(false);

        if (isViewMode) {
            profileWrapper.setVisibility(View.GONE);
            bottomPanel.setVisibility(View.GONE);
            setupTicketUI();
        } else {
            setupBookingUI();
        }

        adapter = new StopSegmentAdapter();
        rvSegments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSegments.setAdapter(adapter);

        loadStops();
    }

    private void setupBookingUI() {
        profilesViewModel = new ViewModelProvider(this).get(ProfilesViewModel.class);
        profileAdapter = new ProfileSelectAdapter(profile -> selectedProfileId = profile.getId());
        rvProfiles.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvProfiles.setAdapter(profileAdapter);

        profilesViewModel.getProfiles().observe(getViewLifecycleOwner(), profiles -> {
            profileAdapter.setProfiles(profiles);
            Profile selected = profileAdapter.getSelectedProfile();
            if (selected != null) {
                selectedProfileId = selected.getId();
            }
        });

        profilesViewModel.loadProfiles();

        if (travel != null && travel.getPrice() != null) {
            Locale pl = new Locale("pl", "PL");
            tvTotalPrice.setText(String.format(pl, "Cena biletu: %.2f zł", travel.getPrice()));
        }

        if (travel != null && travel.getActualDeparture() != null) {
            LocalDateTime dep = LocalDateTime.parse(travel.getActualDeparture());
            tvTripDate.setText(dep.format(
                    DateTimeFormatter.ofPattern("EEEE, d MMMM", new Locale("pl", "PL"))
            ));
        }

        btnContinue.setOnClickListener(v -> {
            if (selectedProfileId == null) {
                Toast.makeText(requireContext(), "Wybierz profil", Toast.LENGTH_SHORT).show();
                return;
            }
            SeatMapFragment nextFrag = SeatMapFragment.newInstance(travel, selectedProfileId);
            if (requireActivity() instanceof SearchTravelActivity) {
                ((SearchTravelActivity) requireActivity()).setCurrentFragment(nextFrag, true);
            }
        });
    }

    private void setupTicketUI() {
        if (ticket != null) {
            if (ticket.getPrice() != null) {
                Locale pl = new Locale("pl", "PL");
                tvTotalPrice.setText(String.format(pl, "Cena biletu: %.2f zł", ticket.getPrice()));
            }
            if (ticket.getActualDeparture() != null) {
                tvTripDate.setText(ticket.getActualDeparture().format(
                        DateTimeFormatter.ofPattern("EEEE, d MMMM", new Locale("pl", "PL"))
                ));
            }
        }
    }

    private void loadStops() {
        Long travelId = null;
        Integer stopFrom = null;
        Integer stopTo = null;

        if (isViewMode && ticket != null) {
            travelId = ticket.getTravelId();
            stopFrom = ticket.getTravelStopNumberFrom();
            stopTo = ticket.getTravelStopNumberTo();
        } else if (travel != null) {
            travelId = travel.getTravelId();
            stopFrom = travel.getTravelStopNumberFrom();
            stopTo = travel.getTravelStopNumberTo();
        }

        if (travelId == null) return;
        setUiState(true, false, false);

        final Integer finalStopFrom = stopFrom;
        final Integer finalStopTo = stopTo;

        TravelRepository.getInstance().getStopsByTravelId(
                travelId,
                new TravelRepository.StopsCallback() {
                    @Override
                    public void onSuccess(List<TravelStop> stops) {
                        if (!isAdded()) return;
                        List<StopSegmentAdapter.Segment> segments = buildSegments(stops, finalStopFrom, finalStopTo);
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

    private List<StopSegmentAdapter.Segment> buildSegments(List<TravelStop> allStops, Integer stopFrom, Integer stopTo) {
        List<StopSegmentAdapter.Segment> result = new ArrayList<>();
        if (allStops == null || allStops.isEmpty() || stopFrom == null || stopTo == null) return result;

        TravelStop fromStop = null;
        for (TravelStop s : allStops) {
            if (s.getStopNumber().equals(stopFrom)) {
                fromStop = s;
                break;
            }
        }
        if (fromStop == null) return result;

        LocalDateTime actualDep = null;
        String trainName = "–";

        if (isViewMode && ticket != null) {
            actualDep = ticket.getActualDeparture();
            trainName = ticket.getTrainName();
        } else if (travel != null) {
            actualDep = LocalDateTime.parse(travel.getActualDeparture());
            trainName = travel.getTrainName();
        }

        if (actualDep == null) return result;

        Duration fromOffset = Duration.parse(fromStop.getDepartureOffset());
        LocalDateTime baseDeparture = actualDep.minus(fromOffset);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        List<TravelStop> relevant = new ArrayList<>();
        for (TravelStop s : allStops) {
            if (s.getStopNumber() >= stopFrom && s.getStopNumber() <= stopTo) {
                relevant.add(s);
            }
        }
        relevant.sort(Comparator.comparingInt(TravelStop::getStopNumber));

        for (int i = 0; i < relevant.size() - 1; i++) {
            TravelStop dep = relevant.get(i);
            TravelStop arr = relevant.get(i + 1);

            LocalDateTime depTime = baseDeparture.plus(Duration.parse(dep.getDepartureOffset()));
            LocalDateTime arrTime = baseDeparture.plus(Duration.parse(arr.getArrivalOffset()));

            int distDiff = arr.getDistance() - dep.getDistance();

            result.add(new StopSegmentAdapter.Segment(
                    depTime.format(fmt),
                    arrTime.format(fmt),
                    dep.getStationName(),
                    arr.getStationName(),
                    trainName != null ? trainName : "–",
                    distDiff
            ));
        }
        return result;
    }

    private void setUiState(boolean loading, boolean showList, boolean showErr) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        rvSegments.setVisibility(showList ? View.VISIBLE : View.GONE);
        errorLayout.setVisibility(showErr ? View.VISIBLE : View.GONE);
        if (loading) btnRetry.setVisibility(View.GONE);
    }

    private void showError(String msg) {
        setUiState(false, false, true);
        tvErrorMsg.setText(msg);
    }
}
