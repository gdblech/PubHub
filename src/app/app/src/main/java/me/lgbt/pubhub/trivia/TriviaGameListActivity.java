package me.lgbt.pubhub.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.trivia.creation.GameSlideCreationActivity;
import me.lgbt.pubhub.trivia.utils.GameAdapter;

public class TriviaGameListActivity extends AppCompatActivity {
    private String phbToken;
    private String googleToken;
    private ArrayList<String> listOfGames; //TODO needs to be populated


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_game_list);
        FloatingActionButton newGame = findViewById(R.id.newGame);
        RecyclerView gameList = findViewById(R.id.gameList);

        unPack();
        fetchGameList();

        GameAdapter adapter = new GameAdapter(listOfGames);
        gameList.setAdapter(adapter);
        gameList.setLayoutManager(new LinearLayoutManager(this));

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view);
            }
        });
    }

    public void fetchGameList() {
        listOfGames = new ArrayList<>();
        listOfGames.add("Test Game Please Ignore");
    }

    public void sendMessage(View view) {
        Intent nextActivity = new Intent(this, GameSlideCreationActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    /* Use this method somewhere without google token */

    public void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            googleToken = data.getString(IntentKeys.GOOGLE); // TODO DELETE this line
        }
    }
}
