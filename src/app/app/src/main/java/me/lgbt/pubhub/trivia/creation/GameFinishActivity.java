package me.lgbt.pubhub.trivia.creation;

/**
 * @author Geoffrey Blech
 * Proccess a game of Trivia and sending it to the backend
 * @since 10/13/2018
 */

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
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
        Button checkName = findViewById(R.id.checkNameButton);
        Button upload = findViewById(R.id.uploadButton);

        progressBar.setVisibility(View.INVISIBLE);

        unPack();

        if (currentGame != null) {
            progressBar.setMax(currentGame.getTotalCount());
        }

        checkName.setOnClickListener(this);
        upload.setOnClickListener(this);
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
        switch (view.getId()) {
            case R.id.uploadButton:
                progressBar.setVisibility(View.VISIBLE);
                createJson();

                break;
            case R.id.checkNameButton:
                break;
        }
    }

    void createJson() {
        StringBuilder json = new StringBuilder();
        json.append('{');
        json.append(" \"host\": \"").append(currentGame.getGameName()).append("\", ");
        json.append(" \"title\": \"").append(currentGame.getTitle()).append("\", ");
        json.append(" \"text\": \"").append(currentGame.getText()).append("\", ");
        json.append(" \"picture\": \"").append(picToBase64(currentGame.getPicture())).append("\", ");

        progressBar.incrementProgressBy(1);
        for (TriviaRound r : currentGame.getRounds()) {
            json.append('{');
            json.append(" \"title\": \"").append(r.getTitle()).append("\", ");
            json.append(" \"text\": \"").append(r.getText()).append("\", ");
            json.append(" \"picture\": \"").append(picToBase64(r.getPicture())).append("\", ");

            progressBar.incrementProgressBy(1);
            for (TriviaQuestion q : r.getQuestions()) {
                json.append('{');
                json.append(" \"title\": \"").append(q.getTitle()).append("\", ");
                json.append(" \"text\": \"").append(q.getText()).append("\", ");
                json.append(" \"answer\": \"").append(q.getAnswer()).append("\", ");
                json.append(" \"picture\": \"").append(picToBase64(q.getPicture())).append("\", ");

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
                //       ServerRestConnection.pushTriviaGame(getString(R.string.phb_url), jsonGame, phbToken);
            }
        });
    }

    private String picToBase64(Uri pictureUri) {
        String base64Pic = "";
        try {

            ContentResolver cr = getContentResolver();
            cr.takePersistableUriPermission(pictureUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Bitmap picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pictureUri);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] picBytes = stream.toByteArray();

            base64Pic = Base64.encodeToString(picBytes, Base64.DEFAULT);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Pic;
    }
}
