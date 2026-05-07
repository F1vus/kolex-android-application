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
import edu.at.kolex.model.Route;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private final List<Route> routes = new ArrayList<>();
    private final OnRouteClickListener listener;

    public interface OnRouteClickListener {
        void onRouteClick(Route route);
    }

    public RouteAdapter(OnRouteClickListener listener) {
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
        Route route = routes.get(position);

        holder.tvTrainNumber.setText(route.getTrainName() != null ? route.getTrainName() : "-");
        holder.tvFromStation.setText(route.getFromStationName() != null ? route.getFromStationName() : "-");
        holder.tvToStation.setText(route.getToStationName() != null ? route.getToStationName() : "-");
        holder.tvDepTime.setText(formatDeparture(route.getActualDeparture()));

        holder.tvPrice.setText(
                route.getPrice() != null
                        ? String.format(Locale.getDefault(), "%.2f PLN", route.getPrice())
                        : "- PLN"
        );

        holder.itemView.setOnClickListener(v -> listener.onRouteClick(route));
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public void updateRoutes(List<Route> newRoutes) {
        routes.clear();
        if (newRoutes != null) {
            routes.addAll(newRoutes);
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
        TextView tvTrainNumber, tvDepTime, tvFromStation, tvToStation, tvPrice;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainNumber = itemView.findViewById(R.id.tvTrainNumber);
            tvDepTime = itemView.findViewById(R.id.tvDepTime);
            tvFromStation = itemView.findViewById(R.id.tvFromStation);
            tvToStation = itemView.findViewById(R.id.tvToStation);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}