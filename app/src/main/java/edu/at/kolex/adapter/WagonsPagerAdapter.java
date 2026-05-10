package edu.at.kolex.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.at.kolex.R;
import edu.at.kolex.model.SeatStatus;

public class WagonsPagerAdapter extends RecyclerView.Adapter<WagonsPagerAdapter.WagonViewHolder> {

    public interface OnSeatClickListener {
        void onSeatClick(SeatStatus seat);
    }

    private List<List<SeatStatus>> wagons;
    private final OnSeatClickListener listener;
    private int selectedSeatNumber = -1;

    public WagonsPagerAdapter(List<List<SeatStatus>> wagons, OnSeatClickListener listener) {
        this.wagons = wagons;
        this.listener = listener;
    }

    public void setData(List<List<SeatStatus>> newData) {
        this.wagons = newData;
        notifyDataSetChanged();
    }

    public void setSelectedSeat(int seatNumber) {
        this.selectedSeatNumber = seatNumber;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WagonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wagon, parent, false);
        return new WagonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WagonViewHolder holder, int position) {
        List<SeatStatus> wagonSeats = wagons.get(position);
        holder.bind(wagonSeats, position, selectedSeatNumber, listener);
    }

    @Override
    public int getItemCount() {
        return wagons == null ? 0 : wagons.size();
    }

    static class WagonViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvWagonTitle;
        private final LinearLayout rowContainer;

        WagonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWagonTitle = itemView.findViewById(R.id.tvWagonTitle);
            rowContainer = itemView.findViewById(R.id.rowContainer);
        }

        void bind(List<SeatStatus> seats,
                  int wagonIndex,
                  int selectedSeatNumber,
                  OnSeatClickListener listener) {

            tvWagonTitle.setText( + (wagonIndex + 1));
            rowContainer.removeAllViews();

            Context context = itemView.getContext();

            int seatSize = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    42,
                    context.getResources().getDisplayMetrics()
            );

            int gapWidth = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    28,
                    context.getResources().getDisplayMetrics()
            );

            // 20 seats = 5 rows x 4 seats
            for (int row = 0; row < 5; row++) {
                LinearLayout seatRow = new LinearLayout(context);
                seatRow.setOrientation(LinearLayout.HORIZONTAL);
                seatRow.setGravity(Gravity.CENTER_HORIZONTAL);
                seatRow.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                for (int col = 0; col < 4; col++) {
                    int seatIndex = row * 4 + col;

                    if (col == 2) {
                        View gap = new View(context);
                        LinearLayout.LayoutParams gapParams = new LinearLayout.LayoutParams(
                                gapWidth,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        );
                        gap.setLayoutParams(gapParams);
                        seatRow.addView(gap);
                    }

                    if (seatIndex >= seats.size()) {
                        View emptySpace = new View(context);
                        LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams(
                                seatSize,
                                seatSize
                        );
                        emptyParams.setMargins(8, 8, 8, 8);
                        emptySpace.setLayoutParams(emptyParams);
                        seatRow.addView(emptySpace);
                        continue;
                    }

                    SeatStatus seat = seats.get(seatIndex);

                    TextView seatView = new TextView(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(seatSize, seatSize);
                    params.setMargins(8, 8, 8, 8);
                    seatView.setLayoutParams(params);
                    seatView.setGravity(Gravity.CENTER);
                    seatView.setText(String.valueOf(seat.getSeatNumber()));
                    seatView.setTextColor(Color.BLACK);
                    seatView.setTextSize(14);
                    seatView.setTypeface(Typeface.DEFAULT_BOLD);

                    if (!seat.isAvailable()) {
                        seatView.setBackgroundResource(R.drawable.bg_seat_taken);
                        seatView.setEnabled(false);
                    } else if (seat.getSeatNumber() == selectedSeatNumber) {
                        seatView.setBackgroundResource(R.drawable.bg_seat_selected);
                        seatView.setEnabled(true);
                    } else {
                        seatView.setBackgroundResource(R.drawable.bg_seat_free);
                        seatView.setEnabled(true);
                    }

                    seatView.setOnClickListener(v -> {
                        if (seat.isAvailable()) {
                            listener.onSeatClick(seat);
                        }
                    });

                    seatRow.addView(seatView);
                }

                rowContainer.addView(seatRow);
            }
        }
    }
}