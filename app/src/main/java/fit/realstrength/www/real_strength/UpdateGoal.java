package fit.realstrength.www.real_strength;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateGoal extends AppCompatActivity {

    private Button btnUpdateGoal;
    private RadioGroup radioGroupGoal;
    public String updateGoal;

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_goal);

        btnUpdateGoal = findViewById(R.id.btnGoal);
        radioGroupGoal = findViewById(R.id.radioGroupGoal);

        btnUpdateGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroupGoal.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                } else {
                    updateDatabase();
                    //ProfileHomePage.tvGoal.setText(updateGoal);
                    finish();
                }
            }
        });
        radioGroupGoal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    updateGoal = "No item selected";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbLoseWeight) {
                    updateGoal = "Lose Weight";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbBuildMuscle) {
                    updateGoal = "Build Muscle";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbTransform) {
                    updateGoal = "Transform";
                }

            }
        });

    }
    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberGoal").setValue(updateGoal);
    }
}
