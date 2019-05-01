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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GoalActivity extends AppCompatActivity {

    private Button btnGoal;
    private ProgressBar progressGoal;
    private RadioGroup radioGroupGoal;
    private String goalSelected = "Select your Goal";
    RadioButton rdBuild, rdLose, rdTransform;

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        btnGoal = findViewById(R.id.btnGoal);
        progressGoal = findViewById(R.id.progressGoal);
        radioGroupGoal = findViewById(R.id.radioGroupGoal);

        rdBuild = findViewById(R.id.rbBuildMuscle);
        rdLose = findViewById(R.id.rbLoseWeight);
        rdTransform = findViewById(R.id.rbTransform);


        progressGoal.setProgress(20);


        btnGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroupGoal.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                } else {
                    updateDatabase();
                    startActivity(new Intent(GoalActivity.this, LevelActivity.class));


                }

            }
        });
        radioGroupGoal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    goalSelected = "No item selected";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbBuildMuscle) {
                    goalSelected = "Build Muscle";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbLoseWeight) {
                    goalSelected = "Lose Weight";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbTransform) {
                    goalSelected = "Transform";
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
                startActivity(new Intent(GoalActivity.this, LevelActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }


    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberGoal").setValue(goalSelected);
    }
}
