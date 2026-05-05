package edu.at.kolex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import edu.at.kolex.R;
import edu.at.kolex.model.Ticket;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private final List<Ticket> tickets;
    private final OnTicketClickListener listener;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

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

        holder.tvTrainNumber.setText(
                ticket.getTrainNumber() != null ? ticket.getTrainNumber() : "-"
        );

        holder.tvSeatId.setText(
                ticket.getPrice() != null ? ticket.getPrice() + " PLN" : "- PLN"
        );

        holder.tvStartStation.setText(
                ticket.getStartStation() != null ? ticket.getStartStation() : "-"
        );

        holder.tvEndStation.setText(
                ticket.getEndStation() != null ? ticket.getEndStation() : "-"
        );

        LocalDateTime departure = ticket.getDepartureDate();

        if (departure != null) {
            holder.tvDepartureDate.setText(departure.format(DATE_FORMAT));
            holder.tvDepartureTime.setText(departure.format(TIME_FORMAT));
        } else {
            holder.tvDepartureDate.setText("-");
            holder.tvDepartureTime.setText("-");
        }

        holder.tvTicketId.setText(
                ticket.getTicketId() != null ? "Ticket #" + ticket.getTicketId() : "Ticket #-"
        );

        holder.itemView.setOnClickListener(v -> listener.onTicketClick(ticket));
    }

    @Override
    public int getItemCount() {
        return tickets != null ? tickets.size() : 0;
    }

    public void updateTickets(List<Ticket> newTickets) {
        tickets.clear();
        tickets.addAll(newTickets);
        notifyDataSetChanged();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {

        TextView tvDepartureDate, tvDepartureTime, tvStartStation, tvEndStation,
                tvSeatId, tvTicketId, tvTrainNumber;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDepartureDate = itemView.findViewById(R.id.tvTopInfo);
            tvDepartureTime = itemView.findViewById(R.id.tvDepartureTime);
            tvStartStation = itemView.findViewById(R.id.tvStartStation);
            tvEndStation = itemView.findViewById(R.id.tvEndStation);
            tvSeatId = itemView.findViewById(R.id.tvSeatId);
            tvTicketId = itemView.findViewById(R.id.tvTicketId);
            tvTrainNumber = itemView.findViewById(R.id.tvTrainNumber);
        }
    }
}