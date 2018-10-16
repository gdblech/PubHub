package me.lgbt.pubhub.trivia.creation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.trivia.utils.TriviaGame;
import me.lgbt.pubhub.trivia.utils.TriviaQuestion;
import me.lgbt.pubhub.trivia.utils.TriviaRound;

public class GameFinishActivity extends AppCompatActivity implements View.OnClickListener{

    private String phbToken;
    private String googleToken;
    private TriviaGame currentGame;
    private String jsonGame;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finish);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setMax(currentGame.getTotalCount());
        unPack();
    }

    public void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            googleToken = data.getString(IntentKeys.GOOGLE);
            currentGame = data.getParcelable(IntentKeys.GAME);
        }
    }

    @Override
    public void onClick(View view) {

    }

    public void createJson(){
        StringBuilder json = new StringBuilder();
        json.append('{');

        json.append(" \"title\": \"").append(currentGame.getTitle()).append("\", ");
        json.append(" \"text\": \"").append(currentGame.getText()).append("\", ");
        //todo add picture info

        progressBar.incrementProgressBy(1);
        for(TriviaRound r : currentGame.getRounds()){
            json.append('{');
            json.append(" \"title\": \"").append(r.getTitle()).append("\", ");
            json.append(" \"text\": \"").append(r.getText()).append("\", ");
            //todo add picture info
            progressBar.incrementProgressBy(1);
            for(TriviaQuestion q : r.getQuestions()){
                json.append('{');
                json.append(" \"title\": \"").append(q.getTitle()).append("\", ");
                json.append(" \"text\": \"").append(q.getText()).append("\", ");
                json.append(" \"answer\": \"").append(q.getAnswer()).append("\", ");
                //todo add picture info
                json.append('}');
                progressBar.incrementProgressBy(1);
            }
            json.append('}');
        }

        json.append('}');
    }
}
