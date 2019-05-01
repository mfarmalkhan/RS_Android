package fit.realstrength.www.real_strength;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDoneException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProfileHomePage extends AppCompatActivity {

    protected static TextView tvGoal;
    protected static TextView tvLevel;
    protected static TextView tvAgeRange;
    protected static TextView tvName;
    protected static  TextView tvWeight;
    protected static  TextView tvHeight;
    private TextView tvBmi;


    private ImageButton updateGoal;
    private ImageButton updateLevel;
    private Button updateWeight;
    private Button updateHeight;
    private ImageButton updatePersonal;
    private Button updateAge;

    protected ImageView profilePhoto;
    protected static ImageView finalPhoto;

    StorageReference storage = FirebaseStorage.getInstance().getReference("Profile Pictures");
    String uid;
    protected static Uri imageUri = UploadPhoto.downloadUri;
    protected static String displayName;
    protected static Uri downloadUri;

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    protected static int currentWeight;
    protected static int currentHeight;
    private TextView tvBmiStatus;

    private ProgressDialog progressDialog;



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_home_page);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading your profile...");
        progressDialog.show();


        //initial information
        tvGoal = findViewById(R.id.tvGoal);
        tvLevel = findViewById(R.id.tvLevel);
        tvAgeRange = findViewById(R.id.tvAgeRange);
        tvName = findViewById(R.id.tvDisplayName);
        tvWeight = findViewById(R.id.selectWeight);
        tvHeight = findViewById(R.id.selectHeight);
        tvBmi = findViewById(R.id.calBMI);
        tvBmiStatus = findViewById(R.id.tvBstatus);


        //updated information
        updateGoal = findViewById(R.id.ibGoal);
        updateLevel = findViewById(R.id.ibLevel);
        updateWeight = findViewById(R.id.btnCW);
        updateHeight = findViewById(R.id.btnCH);
        updatePersonal = findViewById(R.id.ibPerson);
        updateAge = findViewById(R.id.ibAge);
        //profile picture
        profilePhoto = findViewById(R.id.profilePhoto);

        displayInfo();
        setPic();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        finalPhoto = profilePhoto;

        //initial database values
        uid = user.getUid();



        updatePersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileHomePage.this,UpdateNameAndPhoto.class));
                finish();
            }
        });

        updateAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(ProfileHomePage.this,UpdateAge.class));
            }
        });



        updateGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileHomePage.this,UpdateGoal.class));
            }
        });

        updateLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileHomePage.this,UpdateLevel.class));

            }
        });

        updateWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileHomePage.this,UpdateWeight.class));

            }
        });

        updateHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileHomePage.this,UpdateHeight.class));

            }
        });

    }
    private void setPic() {
        StorageReference imagRef = storage.child(user.getEmail());
        imagRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                downloadUri = uri;

                Glide.with(getApplicationContext()).load(downloadUri).into(profilePhoto);

            }
        });

    }
    private void displayInfo() {



        DatabaseReference goalDatabase = databaseMembers.child(user.getUid()).child("memberGoal");
        DatabaseReference levelDatabase = databaseMembers.child(user.getUid()).child("memberLevel");
        DatabaseReference ageDatabase = databaseMembers.child(user.getUid()).child("memberAge");
        DatabaseReference nameDatabase = databaseMembers.child(user.getUid()).child("memberName");
        DatabaseReference heightDatabase = databaseMembers.child(user.getUid()).child("memberHeight");
        DatabaseReference weightDatabase = databaseMembers.child(user.getUid()).child("memberWeight");

        goalDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                tvGoal.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        levelDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                tvLevel.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ageDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                tvAgeRange.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nameDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                tvName.setText(value);
                displayName = value;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        weightDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(Integer.class);
                tvWeight.setText(Integer.toString(value) + " kg");
                currentWeight = value;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        heightDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(Integer.class);
                tvHeight.setText(Integer.toString(value) + " cm");
                currentHeight = value;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       //calculate and display BMI and it's status
        displayBMI();

        progressDialog.dismiss();

    }
    private void calculateBMI(int weight,int height) {

        double finalWeight = (double) weight;
        double finalHeight = (double) height;

        finalHeight = finalHeight/100;
        finalHeight = finalHeight*finalHeight;

        double BMI = finalWeight/finalHeight;

        int bmi = (int) BMI;

        String displayBmi = Integer.toString(bmi);

        if (bmi<19) {
            tvBmiStatus.setText("Underweight");
        }
        else if (bmi>25) {
            tvBmiStatus.setText("Overweight");
        }
        else if (bmi>19 && bmi<25) {
            tvBmiStatus.setText("Healthy");
        }

        tvBmi.setText(displayBmi.trim());

    }


    private void displayBMI() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("members").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int weight = dataSnapshot.child("memberWeight").getValue(Integer.class);
                int height = dataSnapshot.child("memberHeight").getValue(Integer.class);


                calculateBMI(weight,height);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
