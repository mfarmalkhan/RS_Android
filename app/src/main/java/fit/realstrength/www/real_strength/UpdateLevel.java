package fit.realstrength.www.real_strength;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateLevel extends AppCompatActivity {

    private Button btnUpdateLevel;
    private RadioGroup radioGroupLevel;
    private String updateLevel = "Select your level";

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_level);

        btnUpdateLevel = findViewById(R.id.btnLevel);
        radioGroupLevel = findViewById(R.id.radioGroupLevel);

        btnUpdateLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroupLevel.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                } else {
                   // ProfileHomePage.tvLevel.setText(updateLevel);
                    updateDatabase();
                    finish();
                }
            }
        });
        radioGroupLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    updateLevel= "No item selected";
                }
                else if (radioGroup.getCheckedRadioButtonId() == R.id.rbBeginner) {
                    updateLevel = "Beginner";
                }
                else if (radioGroup.getCheckedRadioButtonId() == R.id.rbIntermediate) {
                    updateLevel = "Intermediate";
                }
                else if (radioGroup.getCheckedRadioButtonId() == R.id.rbExpert) {
                    updateLevel = "Expert";
                }
            }
        });
    }
    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberLevel").setValue(updateLevel);
    }
}