package fit.realstrength.www.real_strength;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Statistics extends AppCompatActivity {
    ArrayList<BarEntry> barEntries;
    ArrayList<String> barEntryLabels;
    BarDataSet barDataSet;
    BarData barData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


            BarChart barChart = findViewById(R.id.barChart);

            barChart.getAxisLeft().setAxisMaxValue(200);
            barChart.getAxisLeft().setAxisMinValue(30);

            barChart.getAxisRight().setAxisMaxValue(200);
            barChart.getAxisRight().setAxisMinValue(30);

            barEntries = new ArrayList<>();

            barEntryLabels = new ArrayList<>();

            addValuesTobarEntry();
            addWeight();

            //addValuesTobarEntryLabels();

            barDataSet = new BarDataSet(barEntries, "Your Weight");

            barData = new BarData(barEntryLabels, barDataSet);

            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            barChart.setData(barData);

            barChart.animateY(2000);

            barChart.setDescription("");

    }


    private void addValuesTobarEntry() {

        barEntryLabels.add("Jan");
        barEntryLabels.add("Feb");
        barEntryLabels.add("Mar");
        barEntryLabels.add("Apr");
        barEntryLabels.add("May");
        barEntryLabels.add("June");
        barEntryLabels.add("July");
        barEntryLabels.add("Aug");
        barEntryLabels.add("Sept");
        barEntryLabels.add("Oct");
        barEntryLabels.add("Nov");
        barEntryLabels.add("Dec");

    }

    public void addWeight() {
       // int weight = bundle.getInt("weight");
        //barEntries.add(new BarEntry(weight,addMonth()));

        int month = Calendar.getInstance().get(Calendar.MONTH);

        SharedPreferences result = getSharedPreferences("SaveStatistics", Context.MODE_PRIVATE);

        int weight = result.getInt(Integer.toString(month),100);
        barEntries.add(new BarEntry(weight,month));


    }
}
