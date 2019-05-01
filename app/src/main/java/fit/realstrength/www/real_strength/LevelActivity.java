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

public class LevelActivity extends AppCompatActivity {

    private Button btnLevel;
    private ProgressBar progressLevel;
    private RadioGroup radioGroupLevel;
    private String levelSelected = "Select your level";

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        btnLevel = findViewById(R.id.btnLevel);
        progressLevel = findViewById(R.id.progressLevel);
        radioGroupLevel = findViewById(R.id.radioGroupLevel);

        progressLevel.setProgress(40);

        btnLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroupLevel.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(),"Please select an answer",Toast.LENGTH_SHORT).show();
                }
                else {
                    updateDatabase();
                    startActivity(new Intent(getApplicationContext(), AgeActivity.class));

                }

            }
        });
        radioGroupLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    levelSelected = "No item selected";
                }
                else if (radioGroup.getCheckedRadioButtonId() == R.id.rbBeginner) {
                    levelSelected = "Beginner";
                }
                else if (radioGroup.getCheckedRadioButtonId() == R.id.rbIntermediate) {
                    levelSelected = "Intermediate";
                }
                else if (radioGroup.getCheckedRadioButtonId() == R.id.rbExpert) {
                    levelSelected = "Expert";
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
                startActivity(new Intent(getApplicationContext(), AgeActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberLevel").setValue(levelSelected);
    }

}
