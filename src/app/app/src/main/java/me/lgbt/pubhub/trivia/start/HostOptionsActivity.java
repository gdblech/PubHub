package me.lgbt.pubhub.trivia.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import me.lgbt.pubhub.MainActivity;
import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;

public class HostOptionsActivity extends AppCompatActivity implements View.OnClickListener {
    private String phbToken;
    private Button play;
    private Button host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_options);
        play = findViewById(R.id.playGameWaiting);
        host = findViewById(R.id.hostGameWaiting);

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


    private void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
        }
    }

    public void sendMessagePlay() {
        Intent nextActivity = new Intent(this, MainActivity.class);
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
