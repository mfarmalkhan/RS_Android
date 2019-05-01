package fit.realstrength.www.real_strength;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        final EditText etSubject = findViewById(R.id.editText2);
        final EditText etMessage = findViewById(R.id.editText4);
        Button btnSend = findViewById(R.id.button2);



            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etSubject.getText().length() == 0 || etMessage.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please enter all details", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        //intent.setType("message/rfc822");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"farmal222@gmail.com"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
                        intent.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
                        startActivity(Intent.createChooser(intent, "Send E-mail"));
                    }
                }

            });

    }
}
