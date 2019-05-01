package fit.realstrength.www.real_strength;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LowerBody extends AppCompatActivity {

    ListView listview;
    Intent details_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lower_body);

        listview = findViewById(R.id.full_list);
        details_intent = new Intent(LowerBody.this,Workout_Details.class);

        ArrayList<String> arrayList = new ArrayList<>();

        for (int i=0; i<5; i++) {
            arrayList.add("Week " + (i+1) );
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                details_intent.putExtra("part+Week","Lower+" + (position));
                startActivity(new Intent(details_intent));
            }

        });
    }
}
