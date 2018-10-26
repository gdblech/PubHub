package me.lgbt.pubhub.trivia.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.rest.RestGameList;
import me.lgbt.pubhub.trivia.creation.GameSlideCreationActivity;
import me.lgbt.pubhub.trivia.utils.GameAdapter;

public class TriviaGameListActivity extends AppCompatActivity {
    private String phbToken;
    private ArrayList<String> listOfGames;


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
        RestGameList list = new RestGameList(getString(R.string.phb_url), phbToken);
        list.start();
        try {
            list.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONArray jsonList;
        String json = list.getGameList();
        if (json != null && !json.equals("")) {
            try {
                jsonList = new JSONArray(json);
                for (int i = 0; i < jsonList.length(); i++) {
                    JSONObject obj = jsonList.getJSONObject(i);
                    String str = obj.getString("gameName") + ", " +
                            obj.getString("hostName") + ", " + obj.getString("date");
                    if (!str.contains("null")) {
                        listOfGames.add(str);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(View view) {
        Intent nextActivity = new Intent(this, GameSlideCreationActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    public void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
        }
    }
}
