package edu.at.kolex.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.at.kolex.R;

public class TicketDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_ticket_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView train = findViewById(R.id.tvTrain);
        TextView from = findViewById(R.id.tvFrom);
        TextView to = findViewById(R.id.tvTo);
        TextView price = findViewById(R.id.tvPrice);
        TextView date = findViewById(R.id.tvDate);


        String trainNumber = getIntent().getStringExtra("trainNumber");
        String departureTime = getIntent().getStringExtra("departureTime");
        String arrivalTime = getIntent().getStringExtra("arrivalTime");
        String priceValue = getIntent().getStringExtra("price");
        String duration = getIntent().getStringExtra("duration");

        train.setText(trainNumber != null ? trainNumber : "-");
        from.setText(departureTime != null ? departureTime : "-");
        to.setText(arrivalTime != null ? arrivalTime : "-");
        price.setText(priceValue != null ? priceValue : "-");
        date.setText(duration != null ? duration : "-");
    }
}