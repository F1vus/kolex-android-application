package edu.at.kolex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import edu.at.kolex.R;
import edu.at.kolex.model.Ticket;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private final List<Ticket> tickets;
    private final OnTicketClickListener listener;

    private static final DateTimeFormatter TOP_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy", Locale.getDefault());
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());

    public interface OnTicketClickListener {
        void onTicketClick(Ticket ticket);
    }

    public TicketAdapter(List<Ticket> tickets, OnTicketClickListener listener) {
        this.tickets = tickets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);

        // top bar date
        if (ticket.getActualDeparture() != null) {
            holder.tvTopInfo.setText(ticket.getActualDeparture().format(TOP_DATE_FORMATTER));
            holder.tvDepartureTime.setText(ticket.getActualDeparture().format(TIME_FORMATTER));
        } else {
            holder.tvTopInfo.setText("");
            holder.tvDepartureTime.setText("");
        }

        // arrival time
        if (ticket.getActualArrival() != null) {
            holder.tvArrivalTime.setText(ticket.getActualArrival().format(TIME_FORMATTER));
        } else {
            holder.tvArrivalTime.setText("");
        }

        // duration
        if (ticket.getActualDeparture() != null && ticket.getActualArrival() != null) {
            Duration duration = Duration.between(ticket.getActualDeparture(), ticket.getActualArrival());

            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;

            holder.tvDuration.setText(hours + "h " + minutes + "m");
        } else {
            holder.tvDuration.setText("");
        }

        // stations
        holder.tvStartStation.setText(
                ticket.getFromStationName() != null
                        ? ticket.getFromStationName()
                        : ("Stop " + ticket.getTravelStopNumberFrom())
        );

        holder.tvEndStation.setText(
                ticket.getToStationName() != null
                        ? ticket.getToStationName()
                        : ("Stop " + ticket.getTravelStopNumberTo())
        );

        // passenger
        holder.tvPassengerName.setText(
                ticket.getProfileName() != null ? ticket.getProfileName() : "-"
        );

        // train
        holder.tvTrainName.setText(
                ticket.getTrainName() != null ? ticket.getTrainName() : "-"
        );

        // seat
        holder.tvSeatId.setText(
                ticket.getSeatNumber() != null ? String.valueOf(ticket.getSeatNumber()) : "-"
        );

        holder.itemView.setOnClickListener(v -> listener.onTicketClick(ticket));
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public void updateTickets(List<Ticket> newTickets) {
        this.tickets.clear();
        this.tickets.addAll(newTickets);
        notifyDataSetChanged();
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
        notifyItemInserted(this.tickets.size() - 1);
    }

    public void removeTicket(int position) {
        if (position >= 0 && position < this.tickets.size()) {
            this.tickets.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopInfo, tvDepartureTime, tvArrivalTime, tvDuration,
                tvStartStation, tvEndStation, tvPassengerName, tvTrainName, tvSeatId;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopInfo = itemView.findViewById(R.id.tvTopInfo);
            tvDepartureTime = itemView.findViewById(R.id.tvDepartureTime);
            tvArrivalTime = itemView.findViewById(R.id.tvArrivalTime);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvStartStation = itemView.findViewById(R.id.tvStartStation);
            tvEndStation = itemView.findViewById(R.id.tvEndStation);
            tvPassengerName = itemView.findViewById(R.id.tvPassengerName);
            tvTrainName = itemView.findViewById(R.id.tvTrainName);
            tvSeatId = itemView.findViewById(R.id.tvSeatId);
        }
    }
}