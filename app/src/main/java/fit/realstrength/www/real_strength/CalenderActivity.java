package fit.realstrength.www.real_strength;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class CalenderActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        Uri calendarUri = CalendarContract.CONTENT_URI
                .buildUpon()
                .appendPath("time")
                .build();
        startActivity(new Intent(Intent.ACTION_VIEW, calendarUri));
    }

}
