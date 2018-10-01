package com.example.blairgentry.pubhub_java;

//PubHub 2018, Blair Gentry & Geoffrey Blech

import android.content.Intent;
// import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private SignInButton signInButton;
    private String googleToken;
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
        //switch set up for future redundancy, eg. we may have a sign in anonymously button later
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent);
        handleSignInResult(task);
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            googleToken = account.getIdToken();
            authenticate();
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "handleSignInResult:error", e);
            updateUI(null);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        //TODO this is where we move to the next UI
    }

    private void authenticate() {
        try {

            //REST API https setup
            URL server = new URL(getString(R.string.server_backend) + "/api/auth");
            HttpsURLConnection backend = (HttpsURLConnection) server.openConnection();
            backend.setRequestProperty("Authorization","Bearer " + googleToken); //auth needs to be setup
            backend.setRequestMethod("GET");
//            backend.setDoInput(true);

            //set up cache to store messages from the backend
//            long cacheSize = 10 * 1024 * 1024; // 10MB
//            HttpResponseCache backendCache = HttpResponseCache.install(getCacheDir(), cacheSize);

//            backend.getOutputStream().write(googleToken.getBytes()); //token in body.
            if(backend.getResponseCode() == 200) {
                phbToken = backend.getResponseMessage();
            }else{
                //failed to get response
            }

        } catch (IOException e) {
            Log.w(TAG, "authenticate:error", e);
        }


    }
}
