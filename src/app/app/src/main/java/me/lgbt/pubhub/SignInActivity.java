package me.lgbt.pubhub;

/*
 * Copyright 2018 LGBT - PubHub
 *
 * Geoffrey Blech, Blair Gentry, Linh Tran, Travis Cox
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

import android.content.Intent;
import android.content.res.Resources;
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

import org.json.JSONException;
import org.json.JSONObject;

import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.RestConnection;
import me.lgbt.pubhub.trivia.start.HostOptionsActivity;


/**
 * @author Geoffrey Blech
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<GoogleSignInAccount> {

    private static final int REQ_CODE = 13374;
    private static final String TAG = "SignInActivity";
    private String googleToken;
    private String phbToken;
    private GoogleSignInClient googleSignInClient;
    private SignInButton signInButton;

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
        unpack();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Task<GoogleSignInAccount> task = googleSignInClient.silentSignIn();
        task.addOnCompleteListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            task.addOnCompleteListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        //switch set up for future redundancy, eg. we may have a sign in anonymously button later
        switch (view.getId()) {
            case R.id.sign_in_button:
                view.setVisibility(View.GONE);
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_CODE);
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        GoogleSignInAccount account;
        try {
            account = completedTask.getResult(ApiException.class);

            if (account != null) {
                googleToken = account.getIdToken();
                authenticate();
            }
            updateUI(account);
        } catch (ApiException e) {
            if (e.getStatusCode() == 4) {
                signIn();
            }
            Log.w(TAG, "signInResult:failed code = " + e.getStatusCode(), e);
            updateUI(null);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null && phbToken != null) {
            sendMessage();
        } else {
            System.out.print("Null account or Token");
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    private void authenticate() {

        RestConnection conn;
        Resources res = getResources();
        if (res.getBoolean(R.bool.backend)) {
            conn = new RestConnection(getString(R.string.testingBackendHTTP), googleToken);
        } else {
            conn = new RestConnection(getString(R.string.phb_url), googleToken);
        }

        conn.start(RestConnection.AUTHENTICATE);

        try {
            conn.join();
            phbToken = conn.getResponse();
        } catch (InterruptedException e) {
            String message = "Thread Error: " + e.getMessage();
            Log.e("Sign in Activity", message);
        }
    }

    private void sendMessage() {
        Intent nextActivity;
        if (isHost()) {
            nextActivity = new Intent(this, HostOptionsActivity.class);
        } else {
            nextActivity = new Intent(this, MainActivity.class);
        }
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putInt(IntentKeys.GAMEID, -1);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    private void unpack() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getBoolean(IntentKeys.SIGNOUT)) {
                googleSignInClient.signOut();
            }
        }
    }


    private boolean isHost() {
        String role = getRole();
        return role.equalsIgnoreCase("Host") || role.equalsIgnoreCase("Admin");
//        return true;
    }

    private String getRole() {
        String role = "customer";
        RestConnection getProfile = new RestConnection(getString(R.string.phb_url), phbToken, RestConnection.FETCHPROFILE);
        getProfile.start();
        try {
            getProfile.join();
        } catch (InterruptedException e) {
            //do nothing role will be set to default
        }

        try {
            role = new JSONObject(getProfile.getResponse()).getString("role");
        } catch (JSONException e) {
            //do nothing role will be set to default
        }

        return role;
    }

    @Override
    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
        handleSignInResult(task);
    }
}