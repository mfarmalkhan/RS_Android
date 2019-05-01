package fit.realstrength.www.real_strength;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UpdateWeight extends AppCompatActivity {

    private Button btnUpdateWeight;
    private NumberPicker updateWeightPicker;
    private int updateWeight = ProfileHomePage.currentWeight;
    private Button btnAddWeight;

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_weight);

        btnUpdateWeight = findViewById(R.id.btnWeight);
        updateWeightPicker = findViewById(R.id.weightPicker);
        btnAddWeight = findViewById(R.id.btnAddWeight);

        updateWeightPicker.setMaxValue(200);
        updateWeightPicker.setMinValue(30);
        updateWeightPicker.setWrapSelectorWheel(false);

        updateWeightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                updateWeight = updateWeightPicker.getValue();
               // updateWeight = Integer.toString(weight) + " kg";
            }
        });

        btnUpdateWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDatabase();
                finish();
            }
        });

        btnAddWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int month = Calendar.getInstance().get(Calendar.MONTH);
                SharedPreferences sharedPreferences = getSharedPreferences("SaveStatistics", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = sharedPreferences.edit();
                editor.putInt(Integer.toString(month),updateWeight);
                editor.apply();

                Toast.makeText(getApplicationContext(),"Statistics updated",Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberWeight").setValue(updateWeight);

    }
}
