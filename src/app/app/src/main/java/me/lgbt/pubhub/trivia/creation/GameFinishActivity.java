package me.lgbt.pubhub.trivia.creation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.ServerRestConnection;
import me.lgbt.pubhub.trivia.utils.TriviaGame;
import me.lgbt.pubhub.trivia.utils.TriviaQuestion;
import me.lgbt.pubhub.trivia.utils.TriviaRound;

public class GameFinishActivity extends AppCompatActivity implements View.OnClickListener {

    private String phbToken;
    private TriviaGame currentGame;
    private String jsonGame;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finish);
        progressBar = findViewById(R.id.progressBar);

        if (currentGame != null) {
            progressBar.setMax(currentGame.getTotalCount());
        }
        unPack();
    }

    public void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            currentGame = data.getParcelable(IntentKeys.GAME);
        }
    }

    @Override
    public void onClick(View view) {

    }

    void createJson() {
        StringBuilder json = new StringBuilder();
        json.append('{');
        json.append(" \"host\": \"").append(currentGame.getHost()).append("\", ");
        json.append(" \"date\": \"").append(currentGame.getDate()).append("\", ");
        json.append(" \"title\": \"").append(currentGame.getTitle()).append("\", ");
        json.append(" \"text\": \"").append(currentGame.getText()).append("\", ");
        json.append(" \"picture\": \"").append(ServerRestConnection.pushPicture(getString(R.string.phb_url),
                currentGame.getPicture(), phbToken)).append("\", ");

        progressBar.incrementProgressBy(1);
        for (TriviaRound r : currentGame.getRounds()) {
            json.append('{');
            json.append(" \"title\": \"").append(r.getTitle()).append("\", ");
            json.append(" \"text\": \"").append(r.getText()).append("\", ");
            json.append(" \"picture\": \"").append(ServerRestConnection.pushPicture(getString(R.string.phb_url),
                    r.getPicture(), phbToken)).append("\", ");

            progressBar.incrementProgressBy(1);
            for (TriviaQuestion q : r.getQuestions()) {
                json.append('{');
                json.append(" \"title\": \"").append(q.getTitle()).append("\", ");
                json.append(" \"text\": \"").append(q.getText()).append("\", ");
                json.append(" \"answer\": \"").append(q.getAnswer()).append("\", ");
                json.append(" \"picture\": \"").append(ServerRestConnection.pushPicture(getString(R.string.phb_url),
                        q.getPicture(), phbToken)).append("\", ");

                json.append('}');
                progressBar.incrementProgressBy(1);
            }
            json.append('}');
        }
        json.append('}');
    }

    void sendGame() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ServerRestConnection.pushTriviaGame(getString(R.string.phb_url), jsonGame, phbToken);
            }
        });
    }
}
