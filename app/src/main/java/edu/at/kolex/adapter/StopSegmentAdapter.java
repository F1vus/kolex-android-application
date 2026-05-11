package edu.at.kolex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.at.kolex.R;

public class StopSegmentAdapter
        extends RecyclerView.Adapter<StopSegmentAdapter.SegmentViewHolder> {

    public static class Segment {
        public final String depTime;
        public final String arrTime;
        public final String depStation;
        public final String arrStation;
        public final String trainName;
        public final int    distanceKm;

        public Segment(String depTime, String arrTime,
                       String depStation, String arrStation,
                       String trainName, int distanceKm) {
            this.depTime = depTime;
            this.arrTime = arrTime;
            this.depStation = depStation;
            this.arrStation = arrStation;
            this.trainName = trainName;
            this.distanceKm = distanceKm;
        }
    }

    private final List<Segment> segments = new ArrayList<>();

    @NonNull
    @Override
    public SegmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stop_segment, parent, false);
        return new SegmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SegmentViewHolder h, int position) {
        Segment s = segments.get(position);
        h.tvDepTime.setText(s.depTime);
        h.tvArrTime.setText(s.arrTime);
        h.tvDepStation.setText(s.depStation);
        h.tvArrStation.setText(s.arrStation);
        h.tvTrainName.setText(s.trainName);
        h.tvDistance.setText(s.distanceKm + " km");
    }

    @Override
    public int getItemCount() { return segments.size(); }

    public void setSegments(List<Segment> list) {
        segments.clear();
        if (list != null) segments.addAll(list);
        notifyDataSetChanged();
    }

    public List<Segment> getSegments() {
        return new ArrayList<>(segments);
    }

    static class SegmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepTime, tvArrTime, tvDepStation, tvArrStation, tvTrainName, tvDistance;

        SegmentViewHolder(@NonNull View v) {
            super(v);
            tvDepTime = v.findViewById(R.id.tvSegDepTime);
            tvArrTime = v.findViewById(R.id.tvSegArrTime);
            tvDepStation = v.findViewById(R.id.tvSegDepStation);
            tvArrStation = v.findViewById(R.id.tvSegArrStation);
            tvTrainName = v.findViewById(R.id.tvSegTrainName);
            tvDistance = v.findViewById(R.id.tvSegDistance);
        }
    }
}