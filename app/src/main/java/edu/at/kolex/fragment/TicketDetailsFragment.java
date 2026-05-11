package edu.at.kolex.fragment;

import android.net.Uri;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import edu.at.kolex.R;
import edu.at.kolex.adapter.StopSegmentAdapter;
import edu.at.kolex.model.RefundResponseDto;
import edu.at.kolex.model.Ticket;
import edu.at.kolex.model.TravelStop;
import edu.at.kolex.repository.PaymentRepository;
import edu.at.kolex.repository.TravelRepository;
import edu.at.kolex.utils.PdfUtil;


public class TicketDetailsFragment extends Fragment {

    private static final String ARG_TICKET = "ticket";

    private Ticket ticket;
    private View ticketItem;
    private RecyclerView rvSegments;
    private ProgressBar progressBar;
    private View errorLayout;
    private TextView tvErrorMsg, tvTotalPrice;
    private Button btnRetryDetails, btnRefund, btnDownloadPdf;

    private TextView tvTopInfo,
            tvDepartureTime,
            tvArrivalTime,
            tvDuration,
            tvStartStation,
            tvEndStation,
            tvPassengerName,
            tvTrainName,
            tvSeatId;

    private StopSegmentAdapter adapter;

    public static TicketDetailsFragment newInstance(Ticket ticket) {
        TicketDetailsFragment fragment = new TicketDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TICKET, ticket);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ticket = (Ticket) getArguments().getSerializable(ARG_TICKET);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticket_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ticketItem = view.findViewById(R.id.ticket);
        rvSegments = view.findViewById(R.id.rvSegments);
        progressBar = view.findViewById(R.id.progressBarDetails);
        errorLayout = view.findViewById(R.id.errorLayout);
        tvErrorMsg = view.findViewById(R.id.tvErrorMsg);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnRetryDetails = view.findViewById(R.id.btnRetryDetails);
        btnRefund = view.findViewById(R.id.btnRefund);
        btnDownloadPdf = view.findViewById(R.id.btnDownloadPdf);

        bindTicketItemViews();

        rvSegments.setNestedScrollingEnabled(false);
        adapter = new StopSegmentAdapter();
        rvSegments.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSegments.setAdapter(adapter);

        setupTicketUI();
        setupRefundAction();
        setupPdfFileGenerator();
        loadStops();
    }

    private void bindTicketItemViews() {
        tvTopInfo = ticketItem.findViewById(R.id.tvTopInfo);
        tvDepartureTime = ticketItem.findViewById(R.id.tvDepartureTime);
        tvArrivalTime = ticketItem.findViewById(R.id.tvArrivalTime);
        tvDuration = ticketItem.findViewById(R.id.tvDuration);
        tvStartStation = ticketItem.findViewById(R.id.tvStartStation);
        tvEndStation = ticketItem.findViewById(R.id.tvEndStation);
        tvPassengerName = ticketItem.findViewById(R.id.tvPassengerName);
        tvTrainName = ticketItem.findViewById(R.id.tvTrainName);
        tvSeatId = ticketItem.findViewById(R.id.tvSeatId);
    }

    private void setupPdfFileGenerator(){
        btnDownloadPdf.setOnClickListener(v -> {
            btnDownloadPdf.setEnabled(false);

            PdfUtil.createAndSaveTicketPdf(
                    requireContext(),
                    ticket,
                    adapter != null ? adapter.getSegments() : null,
                    new PdfUtil.PdfCreateCallback() {
                        @Override
                        public void onSuccess(@NonNull Uri uri) {
                            btnDownloadPdf.setEnabled(true);
                            Toast.makeText(requireContext(), "PDF zapisany", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@NonNull String message) {
                            btnDownloadPdf.setEnabled(true);
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
            );
        });
    }

    private void setupTicketUI() {
        if (ticket == null) return;

        Locale pl = new Locale("pl", "PL");

        if (ticket.getPrice() != null) {
            tvTotalPrice.setText(String.format(pl, "Cena biletu: %.2f zł", ticket.getPrice()));
        } else {
            tvTotalPrice.setText("Cena biletu: -");
        }

        if (ticket.getActualDeparture() != null) {
            tvTopInfo.setText(
                    ticket.getActualDeparture().format(
                            DateTimeFormatter.ofPattern("EEEE, d MMMM", pl)
                    )
            );
            tvDepartureTime.setText(
                    ticket.getActualDeparture().format(
                            DateTimeFormatter.ofPattern("HH:mm", pl)
                    )
            );
        } else {
            tvTopInfo.setText("");
            tvDepartureTime.setText("");
        }

        if (ticket.getActualArrival() != null) {
            tvArrivalTime.setText(
                    ticket.getActualArrival().format(
                            DateTimeFormatter.ofPattern("HH:mm", pl)
                    )
            );
        } else {
            tvArrivalTime.setText("");
        }

        if (ticket.getActualDeparture() != null && ticket.getActualArrival() != null) {
            Duration duration = Duration.between(ticket.getActualDeparture(), ticket.getActualArrival());
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            tvDuration.setText(hours + "h " + minutes + "m");
        } else {
            tvDuration.setText("");
        }

        tvStartStation.setText(
                ticket.getFromStationName() != null
                        ? ticket.getFromStationName()
                        : ticket.getTravelStopNumberFrom().toString()
        );

        tvEndStation.setText(
                ticket.getToStationName() != null
                        ? ticket.getToStationName()
                        : ticket.getTravelStopNumberTo().toString()
        );

        tvPassengerName.setText(
                ticket.getProfileName() != null ? ticket.getProfileName() : "-"
        );

        tvTrainName.setText(
                ticket.getTrainName() != null ? ticket.getTrainName() : "-"
        );

        tvSeatId.setText(
                ticket.getSeatNumber() != null ? String.valueOf(ticket.getSeatNumber()) : "-"
        );
    }

    private void setupRefundAction() {
        btnRefund.setOnClickListener(v -> showRefundDialog());
    }

    private void showRefundDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Zwrot biletu")
                .setMessage("Czy na pewno chcesz zwrócić bilet? Otrzymasz zwrot w wysokości jedynie 15% ceny biletu.")
                .setPositiveButton("Tak, zwróć", (dialog, which) -> performRefund())
                .setNegativeButton("Anuluj", null)
                .show();
    }

    private void performRefund() {
        if (ticket == null || ticket.getId() == null) return;

        progressBar.setVisibility(View.VISIBLE);
        btnRefund.setEnabled(false);

        PaymentRepository.getInstance().refundTicket(ticket.getId(), new PaymentRepository.RefundCallback() {
            @Override
            public void onSuccess(RefundResponseDto response) {
                if (!isAdded()) return;

                progressBar.setVisibility(View.GONE);

                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Sukces")
                        .setMessage(
                                response.getMessage()
                                        + "\nZwrócona kwota: "
                                        + response.getRefundAmount()
                                        + " zł"
                        )
                        .setPositiveButton(
                                "OK",
                                (dialog, which) -> requireActivity().getSupportFragmentManager().popBackStack()
                        )
                        .setCancelable(false)
                        .show();
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;

                progressBar.setVisibility(View.GONE);
                btnRefund.setEnabled(true);
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadStops() {
        if (ticket == null) return;

        Long travelId = ticket.getTravelId();
        Integer stopFrom = ticket.getTravelStopNumberFrom();
        Integer stopTo = ticket.getTravelStopNumberTo();

        if (travelId == null || stopFrom == null || stopTo == null) {
            showError("Brak danych trasy");
            return;
        }

        setUiState(true, false, false);

        TravelRepository.getInstance().getStopsByTravelId(
                travelId,
                new TravelRepository.StopsCallback() {
                    @Override
                    public void onSuccess(List<TravelStop> stops) {
                        if (!isAdded()) return;

                        List<StopSegmentAdapter.Segment> segments =
                                buildSegments(stops, stopFrom, stopTo);

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
                        btnRetryDetails.setVisibility(View.VISIBLE);
                        btnRetryDetails.setOnClickListener(v -> loadStops());
                    }

                    @Override
                    public void onNoInternet() {
                        if (!isAdded()) return;
                        showError("Brak połączenia z internetem");
                        btnRetryDetails.setVisibility(View.VISIBLE);
                        btnRetryDetails.setOnClickListener(v -> loadStops());
                    }
                }
        );
    }

    private List<StopSegmentAdapter.Segment> buildSegments(List<TravelStop> allStops,
                                                           Integer stopFrom,
                                                           Integer stopTo) {
        List<StopSegmentAdapter.Segment> result = new ArrayList<>();

        if (allStops == null || allStops.isEmpty() || stopFrom == null || stopTo == null) {
            return result;
        }

        TravelStop fromStop = null;
        for (TravelStop s : allStops) {
            if (s.getStopNumber() != null && s.getStopNumber().equals(stopFrom)) {
                fromStop = s;
                break;
            }
        }

        if (fromStop == null || ticket == null || ticket.getActualDeparture() == null) {
            return result;
        }

        Duration fromOffset = Duration.parse(fromStop.getDepartureOffset());
        LocalDateTime baseDeparture = ticket.getActualDeparture().minus(fromOffset);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        List<TravelStop> relevant = new ArrayList<>();
        for (TravelStop s : allStops) {
            if (s.getStopNumber() != null
                    && s.getStopNumber() >= stopFrom
                    && s.getStopNumber() <= stopTo) {
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
                    ticket.getTrainName() != null ? ticket.getTrainName() : "–",
                    distDiff
            ));
        }

        return result;
    }

    private void setUiState(boolean loading, boolean showList, boolean showErr) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        rvSegments.setVisibility(showList ? View.VISIBLE : View.GONE);
        errorLayout.setVisibility(showErr ? View.VISIBLE : View.GONE);

        if (loading) {
            btnRetryDetails.setVisibility(View.GONE);
        }
    }

    private void showError(String msg) {
        setUiState(false, false, true);
        tvErrorMsg.setText(msg);
    }
}