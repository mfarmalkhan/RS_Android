package fit.realstrength.www.real_strength;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Full_Body extends AppCompatActivity {

    ListView listview;
    Intent details_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full__body);

        listview = findViewById(R.id.full_list);
        details_intent = new Intent(Full_Body.this,Workout_Details.class);

        ArrayList<String> arrayList = new ArrayList<>();

        for (int i=0; i<5; i++) {
            arrayList.add("Week " + (i+1) );
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                details_intent.putExtra("part+Week","Full+" + (position));
                startActivity(new Intent(details_intent));
            }

        });
    }
}
