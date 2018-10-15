package me.lgbt.pubhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.trivia.RoundAdapter;
import me.lgbt.pubhub.trivia.TriviaGame;
import me.lgbt.pubhub.trivia.TriviaRound;

public class RoundListActivity extends AppCompatActivity implements View.OnClickListener {

    private String phbToken;
    private TriviaGame currentGame;
    private RecyclerView roundList;
    private String googleToken;
    private TriviaRound selectedRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_list);
        FloatingActionButton addRoundButton = findViewById(R.id.addRoundButton);
        roundList = findViewById(R.id.roundList);

        unPack();

        RoundAdapter adapter = new RoundAdapter(currentGame.getRounds());
        roundList.setAdapter(adapter);
        roundList.setLayoutManager(new LinearLayoutManager(this));

        addRoundButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addRoundButton:
                sendMessage(view);
                break;
        }
    }

    public void sendMessage(View view) {
        Intent nextActivity = new Intent(this, RoundCreationActivity.class); // add the activity class you're going to, also uncomment duh.
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putString(IntentKeys.GOOGLE, googleToken);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        if (selectedRound != null) {
            extras.putParcelable(IntentKeys.ROUND, selectedRound);
        }
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    public void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            googleToken = data.getString(IntentKeys.GOOGLE);
            currentGame = data.getParcelable(IntentKeys.GAME);
        }
    }
}
