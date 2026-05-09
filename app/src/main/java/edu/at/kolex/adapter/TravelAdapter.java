package edu.at.kolex.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.at.kolex.R;
import edu.at.kolex.model.Travel;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.RouteViewHolder> {

    // Progi cenowe dla kolorów
    private static final double PRICE_LOW = 20.0; // poniżej -> ciemna zieleń
    private static final double PRICE_MED = 50.0;   // poniżej -> żółta zieleń
    // powyżej -> czerwony

    private static final int COLOR_GREEN  = Color.parseColor("#80B918");
    private static final int COLOR_YELLOW = Color.parseColor("#BFD200");
    private static final int COLOR_RED    = Color.parseColor("#B92B18");

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

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        Locale pl = new Locale("pl", "PL");

        LocalDateTime dep = LocalDateTime.parse(travel.getActualDeparture());
        LocalDateTime arr = LocalDateTime.parse(travel.getActualArrival());

        holder.tvDepTime.setText(dep.format(timeFmt));
        holder.tvArrTime.setText(arr.format(timeFmt));
        holder.tvDuration.setText(travel.getDuration());
        holder.tvDateLeft.setText(formatDate(dep, pl));
        holder.tvDateRight.setText(formatDate(arr, pl));

        double price = travel.getPrice() != null ? travel.getPrice() : 0.0;
        holder.tvPrice.setText(String.format(pl, "%.2f", price));

        applyColor(holder, price);

        holder.itemView.setOnClickListener(v -> listener.onRouteClick(travel));
    }

    /** Ustawia kolor paska bocznego, linii i odznaki cenowej na podstawie ceny */
    private void applyColor(@NonNull RouteViewHolder holder, double price) {
        int color;
        if (price < PRICE_LOW) {
            color = COLOR_GREEN;
        } else if (price < PRICE_MED) {
            color = COLOR_YELLOW;
        } else {
            color = COLOR_RED;
        }

        holder.statusBar.setBackgroundColor(color);
        holder.line.setBackgroundColor(color);

        // Zaokrąglone rogi odznaki cenowej
        GradientDrawable priceBg = new GradientDrawable();
        priceBg.setShape(GradientDrawable.RECTANGLE);
        priceBg.setCornerRadius(6f);
        priceBg.setColor(color);
        holder.tvPrice.setBackground(priceBg);
    }

    /** Formatuje datę do "Marzec,\n13 piątek" */
    private String formatDate(LocalDateTime dt, Locale locale) {
        String month = dt.format(DateTimeFormatter.ofPattern("MMMM,", locale));
        month = Character.toUpperCase(month.charAt(0)) + month.substring(1);
        String day = dt.format(DateTimeFormatter.ofPattern("d EEEE", locale));
        return month + "\n" + day;
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

    static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepTime, tvArrTime, tvPrice, tvDuration, tvDateLeft, tvDateRight;
        View statusBar, line;

        RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDepTime = itemView.findViewById(R.id.tvDepTime);
            tvArrTime = itemView.findViewById(R.id.tvArrTime);
            tvPrice = itemView.findViewById(R.id.tvPriceBox);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDateLeft = itemView.findViewById(R.id.tvDateLeft);
            tvDateRight = itemView.findViewById(R.id.tvDateRight);
            statusBar = itemView.findViewById(R.id.statusBar);
            line = itemView.findViewById(R.id.line);
        }
    }
}