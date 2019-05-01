package fit.realstrength.www.real_strength;

import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigationView;
    private TextView tvNavBarName;
    private TextView tvNavBarGoal;
    private View headerView;

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");

    protected static String getUid;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Workout Buttons
    private Button full_body;
    private Button upper_body;
    private Button lower_body;
    private Button abs;




    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //check for database User
        databaseUserExists();


        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);



        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        tvNavBarName = headerView.findViewById(R.id.tvNavBarName);
        tvNavBarGoal = headerView.findViewById(R.id.tvNavBarGoal);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_logOut) {
                    startActivity(new Intent(HomeActivity.this, LogOut.class));
                }
                else if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(HomeActivity.this, ProfileHomePage.class));
                }
                else if (item.getItemId() == R.id.nav_contact) {
                    startActivity(new Intent(HomeActivity.this, ContactUs.class));
                }
                else if (item.getItemId() == R.id.nav_about) {
                    startActivity(new Intent(HomeActivity.this, AboutUs.class));
                }
                else if (item.getItemId() == R.id.nav_statistics) {
                    startActivity(new Intent(HomeActivity.this, Statistics.class));
                }
                else if (item.getItemId() == R.id.nav_schedule) {
                    Uri calendarUri = CalendarContract.CONTENT_URI
                            .buildUpon()
                            .appendPath("time")
                            .build();
                    startActivity(new Intent(Intent.ACTION_VIEW, calendarUri));
                }
                return true;

            }
        });

        //Access Workouts
        full_body = findViewById(R.id.full_body);
        upper_body = findViewById(R.id.btnUpper);
        lower_body = findViewById(R.id.btnLower);
        abs = findViewById(R.id.btnAbs);

        full_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,Full_Body.class));
            }
        });
        upper_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,UpperBody.class));
            }
        });
        lower_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,LowerBody.class));
            }
        });
        abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,AbsBody.class));
            }
        });





        }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void databaseUserExists() {
        String userID = user.getUid();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();
        databaseMembers.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //user does not exist
                    dialog.dismiss();
                    //sendVerificationEmail();
                    startActivity(new Intent(HomeActivity.this, SetProfile.class));
                    finish();
                } else {
                    //user exists
                    dialog.dismiss();
                    //display name and goal in navigation bar
                    displayNavBarInfo();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void displayNavBarInfo() {
        DatabaseReference nameDatabase = databaseMembers.child(user.getUid()).child("memberName");
        DatabaseReference goalDatabase = databaseMembers.child(user.getUid()).child("memberGoal");

        goalDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                tvNavBarGoal.setText("Goal: " + value.toUpperCase());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nameDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                tvNavBarName.setText("Name: " + value.toUpperCase());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    }


