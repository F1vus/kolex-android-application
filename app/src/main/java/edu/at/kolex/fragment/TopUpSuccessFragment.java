package edu.at.kolex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.math.BigDecimal;
import java.util.Locale;

import edu.at.kolex.R;

public class TopUpSuccessFragment extends Fragment {

    private static final String ARG_AMOUNT = "amount";
    private static final String ARG_NEW_BALANCE = "new_balance";

    public static TopUpSuccessFragment newInstance(BigDecimal amount,
                                                   BigDecimal newBalance) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_AMOUNT, amount);
        args.putSerializable(ARG_NEW_BALANCE, newBalance);
        TopUpSuccessFragment f = new TopUpSuccessFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_up_success,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivGif = view.findViewById(R.id.ivSuccessGif);
        TextView  tvAmount = view.findViewById(R.id.tvSuccessAmount);
        MaterialButton btnHome = view.findViewById(R.id.btnGoHome);

        Glide.with(this)
                .asGif()
                .load(R.drawable.success)
                .into(ivGif);

        BigDecimal amount = (BigDecimal) requireArguments()
                .getSerializable(ARG_AMOUNT);
        if (amount != null) {
            tvAmount.setText(String.format(
                    new Locale("pl", "PL"), "+%.2f zł", amount));
        }

        btnHome.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .popBackStack());

    }
}