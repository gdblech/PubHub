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
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import me.lgbt.pubhub.chat.MessageListActivity;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.rest.ConnectionTypes;
import me.lgbt.pubhub.connect.rest.RestAuthenticate;
import me.lgbt.pubhub.trivia.TriviaGameListActivity;
import me.lgbt.pubhub.trivia.creation.GameFinishActivity;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQ_CODE = 13374;
    private static final String TAG = "SignInActivity";
    private String googleToken;
    private String phbToken;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //locate button on the activity gui and set its click behavior
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        Button skipToWorkButton= findViewById(R.id.skip_to_current);
        skipToWorkButton.setOnClickListener(this);

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

        if(!getResources().getBoolean(R.bool.development)) {
            Task<GoogleSignInAccount> task = googleSignInClient.silentSignIn();
            handleSignInResult(task);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    public void onClick(View view) {
        //switch set up for future redundancy, eg. we may have a sign in anonymously button later
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.skip_to_current:
                sendMessage();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_CODE);
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                googleToken = account.getIdToken();
                authenticate();

            }
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code = " + e.getStatusCode(), e);
            updateUI(null);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
//        if(account != null && phbToken != null){
//            Intent nextActivity = new Intent(this, TeamSelectionActivity.class);
//            Bundle extras = new Bundle();
//            extras.putString(IntentKeys.PUBHUB, phbToken);
//            nextActivity.putExtras(extras);
//            startActivity(nextActivity);
//            finish();
//        }else{
//            System.out.print("Null account or Token");
//        }
    }

    private void authenticate() {

        RestAuthenticate auth;
        Resources res = getResources();
        if(res.getBoolean(R.bool.backend)){
            auth = new RestAuthenticate(getString(R.string.testingBackend), googleToken);
        }else{
            auth = new RestAuthenticate(getString(R.string.phb_url), googleToken);
        }

        if(res.getBoolean(R.bool.https)){
            auth.setMode(ConnectionTypes.HTTPS);
        }

        auth.start();

        try {
            auth.join();
            phbToken = auth.getPhbToken();
        } catch (InterruptedException e) {
            String message = "Thread Error: " + e.getMessage();
            Log.e("Sign in Activity", message);
        }
    }

    private void sendMessage() {
        System.out.println("doot doot");
        Intent nextActivity = new Intent(this, MessageListActivity.class); //TODO Change back to TriviaGameActivity
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }
}