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
import java.util.Locale;
import edu.at.kolex.R;
import edu.at.kolex.model.Ticket;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private final List<Ticket> tickets;
    private final OnTicketClickListener listener;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault());
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault());

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        
        // Format departure date (LocalDateTime -> formatted strings)
        LocalDateTime departure = ticket.getDepartureDate();
        if (departure != null) {
            try {
                holder.tvDepartureDate.setText(departure.format(DATE_ONLY_FORMATTER));
                holder.tvDepartureTime.setText(departure.format(TIME_FORMATTER));
            } catch (Exception e) {
                holder.tvDepartureDate.setText("");
                holder.tvDepartureTime.setText("");
            }
        } else {
            holder.tvDepartureDate.setText("");
            holder.tvDepartureTime.setText("");
        }
        
        // Station information
        holder.tvStartStation.setText(
                ticket.getStartStation() != null
                        ? ticket.getStartStation()
                        : holder.itemView.getContext().getString(R.string.stop) + " " + ticket.getStartStopNumber()
        );

        holder.tvEndStation.setText(
                ticket.getEndStation() != null
                        ? ticket.getEndStation()
                        : holder.itemView.getContext().getString(R.string.stop) + " " + ticket.getEndStopNumber()
        );
    // Price
    holder.tvSeatId.setText(ticket.getTicketPrice() != null ? String.format("%s PLN", ticket.getTicketPrice()) : "- PLN");
        
    // Ticket ID (reference) - guard against null id
    holder.tvTicketId.setText( holder.itemView.getContext().getString(R.string.Ticket) + (ticket.getTicketId() != null ? ticket.getTicketId() : ""));
        
        // Train number if available
        if (ticket.getTrainNumber() != null) {
            holder.tvTrainNumber.setText(ticket.getTrainNumber());
            holder.tvTrainNumber.setVisibility(View.VISIBLE);
        } else {
            holder.tvTrainNumber.setVisibility(View.GONE);
        }
        
        holder.itemView.setOnClickListener(v -> listener.onTicketClick(ticket));
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    /**
     * Update the ticket list and refresh the adapter
     */
    public void updateTickets(List<Ticket> newTickets) {
        this.tickets.clear();
        this.tickets.addAll(newTickets);
        notifyDataSetChanged();
    }

    /**
     * Add a single ticket to the list
     */
    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
        notifyItemInserted(this.tickets.size() - 1);
    }

    /**
     * Remove a ticket from the list
     */
    public void removeTicket(int position) {
        if (position >= 0 && position < this.tickets.size()) {
            this.tickets.remove(position);
            notifyItemRemoved(position);
        }
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
