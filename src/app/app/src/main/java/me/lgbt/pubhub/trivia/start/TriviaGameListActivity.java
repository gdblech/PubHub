package me.lgbt.pubhub.trivia.start;

import android.content.Intent;
import android.content.res.Resources;
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

import me.lgbt.pubhub.MainActivity;
import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.RestConnection;
import me.lgbt.pubhub.trivia.creation.GameSlideCreationActivity;
import me.lgbt.pubhub.trivia.utils.interfaces.ClickListener;
import me.lgbt.pubhub.trivia.utils.GameAdapter;
import me.lgbt.pubhub.trivia.utils.GameDisplay;

public class TriviaGameListActivity extends AppCompatActivity implements View.OnClickListener, ClickListener {
    private String phbToken;
    private ArrayList<GameDisplay> listOfGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_game_list);
        FloatingActionButton newGame = findViewById(R.id.newGame);
        RecyclerView gameList = findViewById(R.id.gameList);

        unPack();
        fetchGameList();

        GameAdapter adapter = new GameAdapter(listOfGames, this);
        gameList.setAdapter(adapter);
        gameList.setLayoutManager(new LinearLayoutManager(this));

        newGame.setOnClickListener(this);
    }

    private void fetchGameList() {
        listOfGames = new ArrayList<>();

        RestConnection conn;
        Resources res = getResources();

        if (res.getBoolean(R.bool.backend)) {
            conn = new RestConnection(getString(R.string.testingBackend), phbToken);
        } else {
            conn = new RestConnection(getString(R.string.phb_url), phbToken);
        }
        conn.start(RestConnection.GETGAMES);
        try {
            conn.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONArray jsonList;
        String json = conn.getResponse();
        if (json != null && !json.equals("")) {
            try {
                jsonList = new JSONArray(json);
                for (int i = 0; i < jsonList.length(); i++) {
                    JSONObject obj = jsonList.getJSONObject(i);
                    String name = obj.getString("gameName");
                    GameDisplay game = new GameDisplay(name, obj.getString("hostName"),
                            obj.getString("date"), obj.getInt("id"));
                    if(!name.equals("") && !name.equalsIgnoreCase("null")){
                        listOfGames.add(game);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageCreate() {
        Intent nextActivity = new Intent(this, GameSlideCreationActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    /* This is what takes you to the next activity from the play button. */

    private void sendMessagePlay(int id) {
        Intent nextActivity = new Intent(this, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putBoolean(IntentKeys.HOST, true);
        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putInt(IntentKeys.GAMEID, id); // TODO PASS JSON TO SERVER
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    private void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
        }
    }



    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.newGame){
            sendMessageCreate();
        }
    }

    /* Button handling for edit, delete, and play. Play calls  */

    @Override
    public void onPositionClicked(int position, int button) {
        switch (button){
            case R.id.editButton:{
                break; //todo add edit functionality
            }
            case R.id.deleteButton:{
                break; //todo add delete functionality
            }
            case R.id.playButton:{
                sendMessagePlay(listOfGames.get(position).getId());
            }
        }
    }
}
