package fit.realstrength.www.real_strength;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Height2 extends AppCompatActivity {

    private NumberPicker feetPicker;
    private ProgressBar progressHeight;
    private Button btnHeight;
    private String selectedHeight = "4 ft 0 in";
    String feetValue = "4";
    String inchesValue = "0";
    private int height = 120;

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height2);

        feetPicker = findViewById(R.id.feetPicker);
        progressHeight = findViewById(R.id.progressHeight);
        btnHeight = findViewById(R.id.btnHeight);

        feetPicker.setMaxValue(240);
        feetPicker.setMinValue(120);
        feetPicker.setWrapSelectorWheel(false);


        progressHeight.setProgress(90);


        btnHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDatabase();
                startActivity(new Intent(getApplicationContext(),UploadPhoto.class));
            }
        });
        feetPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                height = feetPicker.getValue();
                feetValue = Integer.toString(feetPicker.getValue()) + " ft";


            }
        });
    }
    public void updateHeight() {
        if (feetValue!=null || inchesValue!=null) {
            selectedHeight = feetValue + " " + inchesValue;
        }
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
                startActivity(new Intent(getApplicationContext(), UploadPhoto.class));


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberHeight").setValue(height);
    }
    private void calculateBMI() {

    }
}
