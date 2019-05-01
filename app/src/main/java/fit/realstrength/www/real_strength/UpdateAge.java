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

public class UpdateAge extends AppCompatActivity {

    protected Button btnUpdateAge;
    private RadioGroup radioGroupAge;
    private String updateAge;

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_age);

        btnUpdateAge = findViewById(R.id.btnAge);
        radioGroupAge = findViewById(R.id.radioGroupAge);

        btnUpdateAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroupAge.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                } else {
                    //ProfileHomePage.tvAgeRange.setText(updateAge);
                    updateDatabase();
                    finish();
                }
            }
        });
        radioGroupAge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    updateAge = "No item selected";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdAge1) {
                    updateAge = "18 - 22";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdAge2) {
                    updateAge = "23 - 28";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdAge3) {
                    updateAge = "29 - 35";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdAge4) {
                    updateAge = "35+";

                }
            }
        });
    }
    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberAge").setValue(updateAge);
    }
}
