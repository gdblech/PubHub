package me.lgbt.pubhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.lgbt.pubhub.Trivia.RoundAdapter;
import me.lgbt.pubhub.Trivia.TriviaGame;
import me.lgbt.pubhub.Trivia.TriviaRound;

public class RoundListActivity extends AppCompatActivity implements View.OnClickListener{

    private String phbToken;
    private TriviaGame currentGame;
    private Bundle data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_list);
        FloatingActionButton addRoundButton = findViewById(R.id.addRoundButton);
        RecyclerView roundList = findViewById(R.id.roundList);
        setUpStart();

        currentGame.addRound(new TriviaRound("Round1", "This is the text", null));
        currentGame.addRound(new TriviaRound("Round2", "This is the text", null));
        currentGame.addRound(new TriviaRound("Round2", "This is the text", null));

        RoundAdapter adapter = new RoundAdapter(currentGame.getRounds());
        roundList.setAdapter(adapter);
        roundList.setLayoutManager(new LinearLayoutManager(this));

        addRoundButton.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setUpStart();
    }

    void setUpStart(){
        data = getIntent().getExtras();
        try {
            phbToken = data.getString("TOKEN");
        } catch (NullPointerException e) {
            phbToken = "No Token Passed";
        }
        setUpGame();

        TriviaRound round = data.getParcelable("TRIVIAROUND");
        if(round != null){
            currentGame.addRound(round);
        }
    }

    void setUpGame() {
        currentGame = data.getParcelable("TRIVIAGAME");
        if (currentGame == null) {
            currentGame = new TriviaGame();
            currentGame.setCreationMode(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addRoundButton:
                sendMessage(view);
                break;
        }
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, RoundCreationActivity.class); // add the activity class you're going to, also uncomment duh.
        intent.putExtra("TOKEN", phbToken);
        startActivity(intent);
    }

}
