package com.example.blairgentry.pubhub_java;

//PubHub 2018, Blair Gentry & Geoffrey Blech

import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                .requestEmail()
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
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    //REST API https setup
                    String url = getString(R.string.server_backend) + "/api/auth";
                    URL server = new URL(url);
                    HttpURLConnection backend = (HttpURLConnection) server.openConnection();
                    backend.setRequestProperty("Authorization", "Bearer " + googleToken);
                    backend.setRequestMethod("GET");
                    backend.connect();

                    //ger response
                    if (backend.getResponseCode() == 200) {
                        BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                        phbToken = response.readLine();
                    } else {
                        throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
                    }

                    backend.disconnect();

                } catch (IOException e) {
                    Log.w(TAG, "Authenticate: error", e);
                }
            }
        });


    }
}
