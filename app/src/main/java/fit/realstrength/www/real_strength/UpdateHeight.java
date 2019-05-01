package fit.realstrength.www.real_strength;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateHeight extends AppCompatActivity {

    private NumberPicker updateFeetPicker;

    private Button btnUpdateHeight;
    private int updateHeight = ProfileHomePage.currentHeight;
    private String feetValue = "4";
    private String inchesValue = "0";

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_height);

        feetValue = "4 ft";
        inchesValue = "0 in";


        updateFeetPicker = findViewById(R.id.feetPicker);

        btnUpdateHeight = findViewById(R.id.btnHeight);


        updateFeetPicker.setMaxValue(240);
        updateFeetPicker.setMinValue(120);
        updateFeetPicker.setWrapSelectorWheel(false);

        btnUpdateHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDatabase();
                finish();
            }
        });

        updateFeetPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                updateHeight = updateFeetPicker.getValue();

            }
        });

    }
    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberHeight").setValue(updateHeight);

    }

}
