package me.lgbt.pubhub.trivia.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.RestConnection;

public class WaitingForGameActivity extends AppCompatActivity implements View.OnClickListener {
    private String phbToken;
    private Button play;
    private Button host;
    private TextView text;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wating_for_game);
        play = findViewById(R.id.playGameWaiting);
        host = findViewById(R.id.hostGameWaiting);
        text = findViewById(R.id.waitingText);
        progressBar = findViewById(R.id.waitingSpinner);

        unPack();
//        if(true){
        if (isHost()) {

            play.setVisibility(View.VISIBLE);
            host.setVisibility(View.VISIBLE);
        } else if (gameStarted()) {
            sendMessagePlay();
        } else {
            text.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        play.setOnClickListener(this);
        host.setOnClickListener(this);
    }


    private boolean isHost() {
        String roll = getRoll();
        return roll.equalsIgnoreCase("Host") || roll.equalsIgnoreCase("Admin");
    }

    private String getRoll(){
        String roll = "customer";
        RestConnection getProfile = new RestConnection(getString(R.string.phb_url), phbToken, RestConnection.FETCHPROFILE);
        getProfile.start();
        try {
            getProfile.join();
        } catch (InterruptedException e) {
            //do nothing roll will be set to default
        }

        try {
            roll = new JSONObject(getProfile.getResponse()).getString("role");
        } catch (JSONException e) {
            //do nothing roll will be set to default
        }

        return roll;
    }

    //todo
    private boolean gameStarted() {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playGameWaiting: {
                play.setVisibility(View.GONE);
                host.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.hostGameWaiting:
                sendMessageHost();
                break;
        }
    }

    //todo add event handler for game start

    private void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
        }
    }

    public void sendMessagePlay() {
        Intent nextActivity = new Intent(this, TeamSelectionActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    public void sendMessageHost() {
        Intent nextActivity = new Intent(this, TriviaGameListActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }
}
