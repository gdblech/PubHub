package me.lgbt.pubhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import me.lgbt.pubhub.connect.IntentKeys;

public class TriviaGameListActivity extends AppCompatActivity {
    private String phbToken;
    private String googleToken;
    private ArrayList<String> listOfGames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_game_list);
        FloatingActionButton newGame = findViewById(R.id.newGame);
        RecyclerView gameList = findViewById(R.id.gameList);

        unPack();


        //TODO @Blair add list of games stuff.

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view);
            }
        });
    }

    public void sendMessage(View view) {
        Intent nextActivity = new Intent(this, GameSlideCreationActivity.class); // add the activity class you're going to, also uncomment duh.
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putString(IntentKeys.GOOGLE, googleToken);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    public void  unPack(){
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            googleToken = data.getString(IntentKeys.GOOGLE);
        }
    }
}
