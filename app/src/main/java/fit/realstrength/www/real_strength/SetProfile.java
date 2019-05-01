package fit.realstrength.www.real_strength;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SetProfile extends AppCompatActivity {

    private Button btnNext;
    private ProgressBar progressHome;

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);


        btnNext = findViewById(R.id.btnNext);
        progressHome = findViewById(R.id.progressHome);

        progressHome.setProgress(0);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMember();
                startActivity(new Intent(getApplicationContext(),GoalActivity.class));
                finish();
            }
        });


    }
    private void addMember() {


        String name = "Your Name";
        String level = "Select Level";
        String age = "Select age range";
        String goal = "Select goal";
        int weight = 30;
        int height = 120;


        String userID = user.getUid();


        String id = databaseMembers.push().getKey();
        Member member = new Member(name, goal, level, age, weight, height);


        databaseMembers.child(userID).setValue(member);


        Toast.makeText(this, "Added member to database", Toast.LENGTH_SHORT).show();

      /*  for (int i = 0; i<12 ; i++){

          SharedPreferences sharedPreferences = getSharedPreferences("SaveStatistics", Context.MODE_PRIVATE);
          SharedPreferences.Editor editor  = sharedPreferences.edit();
          editor.putInt(Integer.toString(i),0);
          editor.apply();


        }
        */
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
