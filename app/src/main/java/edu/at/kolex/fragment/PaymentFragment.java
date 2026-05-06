package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.at.kolex.R;

public class PaymentFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        String train = args != null ? args.getString("train", "-") : "-";
        String from = args != null ? args.getString("from", "-") : "-";
        String to = args != null ? args.getString("to", "-") : "-";
        String price = args != null ? args.getString("price", "-") : "-";

        TextView tvTrain = view.findViewById(R.id.tvPaymentTrain);
        TextView tvRoute = view.findViewById(R.id.tvPaymentRoute);
        TextView tvPrice = view.findViewById(R.id.tvPaymentPrice);
        Button btnPay = view.findViewById(R.id.btnPay);

        tvTrain.setText(train);
        tvRoute.setText(from + " → " + to);
        tvPrice.setText(price);

        btnPay.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Płatność zakończona ✔", Toast.LENGTH_SHORT).show();

            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });
    }
}