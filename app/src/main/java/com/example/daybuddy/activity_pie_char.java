package com.example.daybuddy;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class activity_pie_char extends AppCompatActivity {


    PieChart pieChart;
    int count, countCheck, checked, pending;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pie_char);

        pieChart = findViewById(R.id.pieChart);




        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            count = extras.getInt("count");
            countCheck = extras.getInt("countCheck");
        }


        if (count != 0) {
            checked = (int) ((countCheck * 100) / count);
            pending = (int) (((count - countCheck) * 100) / count);
        } else {
            countCheck = 0;
            pending = 0;
        }

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(checked, "Completed"));
        pieEntries.add(new PieEntry(pending, "Pending"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Statistics");
        pieChart.animate();


    }


    public void Back(View view) {

        finish();

    }
}