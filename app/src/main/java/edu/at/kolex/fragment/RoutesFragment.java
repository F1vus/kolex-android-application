package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.at.kolex.R;
import edu.at.kolex.adapter.RouteAdapter;
import edu.at.kolex.model.Route;

public class RoutesFragment extends Fragment {

    private String departure, arrival;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        departure = getArguments() != null ? getArguments().getString("departure") : "N/A";
        arrival = getArguments() != null ? getArguments().getString("arrival") : "N/A";

        TextView title = view.findViewById(R.id.tvRouteTitle);
        title.setText(departure + " → " + arrival);

        RecyclerView rvRoutes = view.findViewById(R.id.rvRoutes);
        rvRoutes.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup Adapter with Mock Data
        RouteAdapter adapter = new RouteAdapter(getMockRoutes(), route -> {
            Toast.makeText(getContext(), "Selected: " + route.getTrainNumber(), Toast.LENGTH_SHORT).show();
            // TODO: Navigation to RouteDetailsFragment
        });
        rvRoutes.setAdapter(adapter);
    }

    private List<Route> getMockRoutes() {
        List<Route> list = new ArrayList<>();
        list.add(new Route("IC 1620", "08:15", "10:30", "2h 15m", "49.00 PLN"));
        list.add(new Route("TLK 3512", "11:00", "14:20", "3h 20m", "35.50 PLN"));
        list.add(new Route("EIP 1004", "13:45", "15:30", "1h 45m", "120.00 PLN"));
        list.add(new Route("IC 4500", "16:20", "19:05", "2h 45m", "52.00 PLN"));
        list.add(new Route("TLK 2100", "20:00", "23:40", "3h 40m", "29.99 PLN"));
        return list;
    }
}