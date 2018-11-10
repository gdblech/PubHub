package me.lgbt.pubhub.trivia.creation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.trivia.utils.ClickListener;
import me.lgbt.pubhub.trivia.utils.RoundAdapter;
import me.lgbt.pubhub.trivia.utils.TriviaGame;
import me.lgbt.pubhub.trivia.utils.TriviaRound;

public class RoundListActivity extends AppCompatActivity implements View.OnClickListener, ClickListener {

    private String phbToken;
    private TriviaGame currentGame;
    private TriviaRound selectedRound;
    private int roundPosition;
    private RoundAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_list);
        FloatingActionButton addRoundButton = findViewById(R.id.addRoundButton);
        FloatingActionButton doneRoundButton = findViewById(R.id.roundListDoneButton);
        RecyclerView roundList = findViewById(R.id.roundList);

        unPack();

        adapter = new RoundAdapter(currentGame.getRounds(), this);
        roundList.setAdapter(adapter);
        roundList.setLayoutManager(new LinearLayoutManager(this));

        addRoundButton.setOnClickListener(this);
        doneRoundButton.setOnClickListener(this);
    }

    private void doneMessage() {
        Intent doneActivity = new Intent(this, GameFinishActivity.class);
        Bundle extras = new Bundle();

        currentGame.trimQuestion();
        extras.putParcelable(IntentKeys.GAME, currentGame);
        extras.putString(IntentKeys.PUBHUB, phbToken);
        doneActivity.putExtras(extras);

        startActivity(doneActivity);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addRoundButton:
                sendMessage();
                break;
            case R.id.roundListDoneButton:
                doneMessage();
                break;
        }
    }

    private void sendMessage() {
        Intent nextActivity = new Intent(this, RoundCreationActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        if (selectedRound != null) {
            extras.putInt(IntentKeys.RPOSITION, roundPosition);
            extras.putParcelable(IntentKeys.ROUND, selectedRound);
        } else {
            extras.putInt(IntentKeys.RPOSITION, -1);
        }
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    private void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            currentGame = data.getParcelable(IntentKeys.GAME);
        }
    }

    @Override
    public void onPositionClicked(int position, int button) {
        switch (button) {
            case R.id.editButton:
                selectedRound = currentGame.getRounds().get(position);
                roundPosition = position;
                sendMessage();
                break;
            case R.id.deleteButton:
                currentGame.removeRound(position); //todo add confirmation alert dialog box
                adapter.notifyItemRemoved(position);
                break;
        }
    }
}
