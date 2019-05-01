package fit.realstrength.www.real_strength;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class Workout_Details extends AppCompatActivity {

    String details;
    TextView tv_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout__details);

        tv_details = findViewById(R.id.tv_details);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            details = extras.getString("part+Week");
        }


       /* DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));
        */

        //display workout details
        readFile(details);

    }
    public void readFile(String type) {

        String text = "";

        try {
            InputStream is = getAssets().open(type + ".txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            text = new String(buffer);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        tv_details.setText(text);
    }
}
