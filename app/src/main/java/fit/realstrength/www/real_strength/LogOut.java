package fit.realstrength.www.real_strength;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

public class LogOut extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView tvLogOut;
    private Button btnLogOut;
    private Button noLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_log_out);

        tvLogOut = findViewById(R.id.textView8);
        btnLogOut = findViewById(R.id.btnLogOut);
        noLogOut = findViewById(R.id.btnNoLogOut);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (UserInfo user : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {

                    if (user.getProviderId().equals("facebook.com")) {
                        //For linked facebook account
                        Log.d("xx_xx_provider_info", "User is signed in with Facebook");
                       // tvLogOut.setText("Are you sure you want to Log Out your Facebook Account");
                        fbsignOut();


                    } else if (user.getProviderId().equals("google.com")) {
                        //For linked Google account
                        Log.d("xx_xx_provider_info", "User is signed in with Google");
                        googleSignOut();
                        //tvLogOut.setText("Are you sure you want to Log Out your Google Account");
                    }
                }
                startActivity(new Intent(LogOut.this,LoginPage.class));
            }
        });
        noLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void fbsignOut() {
        FirebaseAuth.getInstance().signOut();
        disconnectFromFacebook();
        //LoginManager.getInstance().logOut();
        // Intent login = new Intent(LogOutTest.this, LoginPage.class);
        Intent intent = new Intent(LogOut.this, LoginPage.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finishAffinity();
        startActivity(intent);

    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();

    }

    public void googleSignOut() {

        // Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //updateUI(null);
                        Toast.makeText(getApplicationContext(), "Logged Out of Google Account", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(LogOut.this, LoginPage.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finishAffinity();
                        startActivity(intent);
                    }
                });
    }
}
