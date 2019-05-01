package fit.realstrength.www.real_strength;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgeActivity extends AppCompatActivity {

    private Button btnAge;
    private ProgressBar progressAge;
    private RadioGroup radioGroupAge;
    private String ageSelected = "Select age range";

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);

        btnAge = findViewById(R.id.btnAge);
        progressAge = findViewById(R.id.progressAge);
        radioGroupAge = findViewById(R.id.radioGroupAge);

        progressAge.setProgress(60);

        btnAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroupAge.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                } else {
                    updateDatabase();
                    startActivity(new Intent(getApplicationContext(), WeightActivity.class));

                }
            }
        });
        radioGroupAge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    ageSelected = "No item selected";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdAge1) {
                    ageSelected = "18 - 22";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdAge2) {
                    ageSelected = "23 - 28";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdAge3) {
                    ageSelected = "29 - 35";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdAge4) {
                    ageSelected = "35+";

                }
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
                startActivity(new Intent(getApplicationContext(), WeightActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberAge").setValue(ageSelected);
    }

}