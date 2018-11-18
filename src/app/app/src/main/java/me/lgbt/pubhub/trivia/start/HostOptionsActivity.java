package me.lgbt.pubhub.trivia.start;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import me.lgbt.pubhub.MainActivity;
import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.RestConnection;

public class HostOptionsActivity extends AppCompatActivity implements View.OnClickListener {
    private String phbToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_options);
        Button play = findViewById(R.id.playGameWaiting);
        Button host = findViewById(R.id.hostGameWaiting);
        unPack();
        play.setOnClickListener(this);
        host.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playGameWaiting: {
                sendMessagePlay();
                break;
            }
            case R.id.hostGameWaiting:
                sendMessageHost();
                break;
        }
    }

    public void onGamePlay() {

    }

    private void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
        }
    }

    // sends you to MainActivity
    private void sendMessagePlay() {

//        RestConnection conn;
//        Resources res = getResources();
//        if (res.getBoolean(R.bool.backend)) {
//            conn = new RestConnection(getString(R.string.testingBackend), googleToken);
//        } else {
//            conn = new RestConnection(getString(R.string.phb_url), googleToken);
//        }
//
//        conn.start(RestConnection.GETGAME);
//
//        try {
//            conn.join();
//            phbToken = conn.getResponse();
//        } catch (InterruptedException e) {
//            String message = "Thread Error: " + e.getMessage();
//            Log.e("Host Options Activity Game: ", message);
//        }
//    }
        Intent nextActivity = new Intent(this, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    private void sendMessageHost() {
        Intent nextActivity = new Intent(this, TriviaGameListActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }
}
