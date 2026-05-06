package edu.at.kolex.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.at.kolex.R;

public class PaymentActivity extends AppCompatActivity {

    TextView tvTrain, tvRoute, tvPrice;
    Button btnPay;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvTrain = findViewById(R.id.tvPaymentTrain);
        tvRoute = findViewById(R.id.tvPaymentRoute);
        tvPrice = findViewById(R.id.tvPaymentPrice);
        btnPay = findViewById(R.id.btnPay);

        Intent intent = getIntent();

        String train = intent.getStringExtra("train");
        String from = intent.getStringExtra("from");
        String to = intent.getStringExtra("to");
        String price = intent.getStringExtra("price");

        tvTrain.setText(train != null ? train : "-");
        tvRoute.setText((from != null ? from : "-") + " → " + (to != null ? to : "-"));
        tvPrice.setText(price != null ? price : "-");

        btnPay.setOnClickListener(v -> {
            Toast.makeText(PaymentActivity.this, "Płatność zakończona ✔", Toast.LENGTH_SHORT).show();
        });
    }
}