package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.at.kolex.R;
import edu.at.kolex.adapter.RouteAdapter;
import edu.at.kolex.model.Route;
import edu.at.kolex.model.Ticket;

public class RoutesFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView rv = view.findViewById(R.id.rvRoutes);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        RouteAdapter adapter = new RouteAdapter(getRoutes(), route -> {

            Ticket ticket = new Ticket(
                    route.getTrainNumber(),
                    route.getPrice()
            );

            Bundle b = new Bundle();
            b.putSerializable("ticket", ticket);

            Navigation.findNavController(view)
                    .navigate(R.id.action_routesFragment_to_ticketDetailsFragment, b);
        });

        rv.setAdapter(adapter);
    }

    private List<Route> getRoutes() {
        List<Route> list = new ArrayList<>();
        list.add(new Route("IC 100", "08:00", "10:00", "2h", "50 PLN"));
        list.add(new Route("TLK 200", "09:00", "12:00", "3h", "40 PLN"));
        return list;
    }
}