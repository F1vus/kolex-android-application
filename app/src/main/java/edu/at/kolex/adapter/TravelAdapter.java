package edu.at.kolex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.at.kolex.R;
import edu.at.kolex.model.Travel;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.RouteViewHolder> {

    private final List<Travel> travels = new ArrayList<>();
    private final OnRouteClickListener listener;

    public interface OnRouteClickListener {
        void onRouteClick(Travel travel);
    }

    public TravelAdapter(OnRouteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Travel travel = travels.get(position);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

       // holder.tvTrainNumber.setText(route.getTrainName() != null ? route.getTrainName() : "-");
        holder.tvDepTime.setText(LocalDateTime.parse(travel.getActualDeparture()).format(formatter));
        holder.tvArrTime.setText(LocalDateTime.parse(travel.getActualArrival()).format(formatter));
        holder.tvPrice.setText(
                travel.getPrice() != null
                        ? String.format(Locale.getDefault(), "%.2f PLN", travel.getPrice())
                        : "- PLN"
        );

        holder.itemView.setOnClickListener(v -> listener.onRouteClick(travel));
    }

    @Override
    public int getItemCount() {
        return travels.size();
    }

    public void updateRoutes(List<Travel> newTravels) {
        travels.clear();
        if (newTravels != null) {
            travels.addAll(newTravels);
        }
        notifyDataSetChanged();
    }

    private String formatDeparture(String value) {
        if (value == null || value.isBlank()) return "-";

        try {
            LocalDateTime dateTime = LocalDateTime.parse(value);
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            return value;
        }
    }

    static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrainNumber, tvDepTime, tvArrTime,tvPrice;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
           // tvTrainNumber = itemView.findViewById(R.id.tvTrainNumber);
            tvDepTime = itemView.findViewById(R.id.tvDepTime);
            tvPrice = itemView.findViewById(R.id.tvPriceBox);
            tvArrTime = itemView.findViewById(R.id.tvArrTime);
        }
    }
}