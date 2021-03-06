package me.lgbt.pubhub.trivia.creation;

/**
 * @author Geoffrey Blech
 * Proccess a game of trivia and sending it to the backend
 * @since 10/13/2018
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.RestConnection;
import me.lgbt.pubhub.trivia.start.TriviaGameListActivity;
import me.lgbt.pubhub.trivia.utils.TriviaGame;
import me.lgbt.pubhub.trivia.utils.TriviaQuestion;
import me.lgbt.pubhub.trivia.utils.TriviaRound;

public class GameFinishActivity extends AppCompatActivity implements View.OnClickListener {

    private DialogFragment newFragment;
    private String phbToken;
    private TriviaGame currentGame;
    private String jsonGame;
    private ProgressBar progressBar;
    private EditText gameName;
    private EditText hostName;
    private String host;
    private String gameDate;
    private Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finish);
        progressBar = findViewById(R.id.progressBar);
        upload = findViewById(R.id.uploadButton);
        gameName = findViewById(R.id.gameNameFinish);
        hostName = findViewById(R.id.hostNameFinish);
        Button setDate = findViewById(R.id.selectDateFinish);
        newFragment = new DatePickerFragment();

        progressBar.setVisibility(View.INVISIBLE);

        unPack();

        if (currentGame != null) {
            progressBar.setMax(currentGame.getTotalCount());
        }

        upload.setOnClickListener(this);
        setDate.setOnClickListener(this);
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
                upload.setVisibility(View.GONE);
                String gName = gameName.getText().toString();
                host = hostName.getText().toString();
                gameDate = ((DatePickerFragment) newFragment).getDate();

                if (gName.equals("")) {
                    Toast.makeText(this, "Trivia Game Requires a Name", Toast.LENGTH_SHORT).show();
                } else if (gameDate.equals("")) {
                    Toast.makeText(this, "Trivia Game Requires a Date", Toast.LENGTH_SHORT).show();
                } else if (host.equals("")) {
                    Toast.makeText(this, "Trivia Game Requires a Host", Toast.LENGTH_SHORT).show();
                } else {
                    currentGame.setGameName(gName);
                    progressBar.setVisibility(View.VISIBLE);
                    createJson();
                }
                break;
            case R.id.selectDateFinish: {
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
            break;
        }
    }

    /**
     * Creates the JSON from a game of trivia for sending to the back end.
     */
    void createJson() {
        JSONObject gameJson = new JSONObject();
        try {
            gameJson.put("gameName", currentGame.getGameName());
            gameJson.put("hostName", host);
            gameJson.put("date", gameDate);
            gameJson.put("title", currentGame.getTitle());
            gameJson.put("text", currentGame.getTitle());
            gameJson.put("image", imgToBase64(currentGame.getPicture()));
            progressBar.incrementProgressBy(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray roundArray = new JSONArray();
        int i = 0;
        for (TriviaRound r : currentGame.getRounds()) {
            JSONObject jsonRound = new JSONObject();
            try {
                jsonRound.put("roundNumber", i);
                jsonRound.put("title", r.getTitle());
                jsonRound.put("text", r.getTitle());
                jsonRound.put("image", imgToBase64(r.getPicture()));
                progressBar.incrementProgressBy(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
            JSONArray questArray = new JSONArray();
            int j = 0;
            for (TriviaQuestion q : r.getQuestions()) {
                JSONObject jsonQuest = new JSONObject();
                try {
                    jsonQuest.put("questionNumber", j);
                    jsonQuest.put("title", q.getTitle());
                    jsonQuest.put("text", q.getTitle());
                    jsonQuest.put("image", imgToBase64(q.getPicture()));
                    jsonQuest.put("answer", q.getAnswer());
                    questArray.put(jsonQuest);
                    progressBar.incrementProgressBy(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                j++;
            }
            try {
                jsonRound.put("triviaQuestions", questArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            roundArray.put(jsonRound);
        }
        try {
            gameJson.put("triviaRounds", roundArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonGame = gameJson.toString();
        sendGame();
    }


    /**
     * sends the game to the backend
     */
    void sendGame() {
        RestConnection conn;
        Resources res = getResources();
        if (res.getBoolean(R.bool.backend)) {
            conn = new RestConnection(getString(R.string.testingBackendHTTP), phbToken);
        } else {
            conn = new RestConnection(getString(R.string.phb_url), phbToken);
        }
        conn.setBody(jsonGame);

        conn.start(RestConnection.SENDGAME);
        try {
            conn.join();
            sendMessage();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Upload Interrupted, Try Again", Toast.LENGTH_LONG).show();
            upload.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Converts a picture saved on the phones storage device and converts it to WEBP format with 85% quality and the to base64
     *
     * @param imageUri URI of the image
     * @return returns the String representation of the converted image
     */
    private String imgToBase64(Uri imageUri) {
        String base64Pic = "";
        try {
            ContentResolver cr = getContentResolver();
            cr.takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Bitmap picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.WEBP, 85, stream);
            byte[] picBytes = stream.toByteArray();

            base64Pic = Base64.encodeToString(picBytes, Base64.DEFAULT);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Pic.replaceAll("\n", "");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void sendMessage() {
        Intent nextActivity = new Intent(this, TriviaGameListActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    /**
     * class for showing and returning the date of the game from the host.
     */
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private String date = "";

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current gameDate as the default gameDate in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            StringBuilder builder = new StringBuilder();
            if (month < 10) {
                builder.append(0);
            }
            builder.append(month).append("/");
            if (day < 10) {
                builder.append(0);
            }
            builder.append(day).append("/").append(year);
            date = builder.toString();
        }

        public String getDate() {
            return date;
        }
    }
}

