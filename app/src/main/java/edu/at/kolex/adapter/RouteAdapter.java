package edu.at.kolex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.at.kolex.R;
import edu.at.kolex.model.Route;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private final List<Route> routes;
    private final OnRouteClickListener listener;

    public interface OnRouteClickListener {
        void onRouteClick(Route route);
    }

    public RouteAdapter(List<Route> routes, OnRouteClickListener listener) {
        this.routes = routes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routes.get(position);
        holder.tvTrainNumber.setText(route.getTrainNumber());
        holder.tvDepTime.setText(route.getDepartureTime());
        holder.tvArrTime.setText(route.getArrivalTime());
        holder.tvDuration.setText(route.getDuration());
        holder.tvPrice.setText(route.getPrice());
        holder.itemView.setOnClickListener(v -> listener.onRouteClick(route));
    }

    @Override
    public int getItemCount() { return routes.size(); }

    static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrainNumber, tvDepTime, tvArrTime, tvDuration, tvPrice;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainNumber = itemView.findViewById(R.id.tvTrainNumber);
            tvDepTime = itemView.findViewById(R.id.tvDepTime);
            tvArrTime = itemView.findViewById(R.id.tvArrTime);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}