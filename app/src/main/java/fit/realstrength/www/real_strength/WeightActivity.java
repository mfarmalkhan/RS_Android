package fit.realstrength.www.real_strength;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class WeightActivity extends AppCompatActivity {

    private NumberPicker weightPicker;
    private Button btnWeight;
    private ProgressBar progressWeight;
    private int selectedWeight = 30;


    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        btnWeight = findViewById(R.id.btnWeight);
        progressWeight = findViewById(R.id.progressWeight);

        weightPicker = findViewById(R.id.weightPicker);
        weightPicker.setMaxValue(200);
        weightPicker.setMinValue(30);
        weightPicker.setWrapSelectorWheel(false);

        progressWeight.setProgress(80);

        btnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDatabase();
                updateStats();

                startActivity(new Intent(getApplicationContext(), Height2.class));

            }
        });
        weightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int weight = weightPicker.getValue();
               // selectedWeight = Integer.toString(weight) + " kg";
                selectedWeight = weightPicker.getValue();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_skip:
                startActivity(new Intent(getApplicationContext(), Height2.class));
                //finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberWeight").setValue(selectedWeight);
    }
    private void updateStats() {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        SharedPreferences sharedPreferences = getSharedPreferences("SaveStatistics", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putInt(Integer.toString(month),selectedWeight);
        editor.apply();
    }


}
