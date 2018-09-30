package com.example.blairgentry.pubhub_java;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private SignInButton signInButton;
    private String idToken;
    private String phbToken;
    private GoogleSignInClient googleSignInClient;
    private static final int REQ_CODE = 13374;
    private static final String TAG = "SignInActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //locate button on the activity gui and set its click behavior
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        //sign in variables
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, signInOptions);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //If the user has already signed in previously, sign them in without the button, and so silently
        googleSignInClient.silentSignIn()
                .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        handleSignInResult(task);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent);
        handleSignInResult(task);
    }
    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            authenticate(account.getIdToken());
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "handleSignInResult:error", e);
            updateUI(null);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account){

    }

    private void authenticate(String googleToken){
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost("pub-hub.us-east-2.elasticbeanstalk.com");
        //temp assignemnt TODO get token from back end.
        idToken = googleToken;
        phbToken = idToken;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }

}
