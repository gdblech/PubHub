package me.lgbt.pubhub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.trivia.TriviaGame;

public class GameSlideCreationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_IMAGE = 125;
    private String phbToken;
    private Uri pictureUri;
    private TriviaGame currentGame;
    private EditText title;
    private EditText gameText;
    private ImageView picture;
    private String googleToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_slide_creation);

        picture = findViewById(R.id.gameCreationImage);
        FloatingActionButton doneButton = findViewById(R.id.gameSlideDone);
        title = findViewById(R.id.gameTitle);
        gameText = findViewById(R.id.gameSlideText);
        picture.setImageResource(R.drawable.add_image_icon);

        unPack();
        setUpGame();

        picture.setOnClickListener(this);
        doneButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pictureUri = data.getData();
            picture.setImageURI(pictureUri);
        }
    }

    void setUpGame() {

        if (currentGame == null) {
            currentGame = new TriviaGame();
            picture.setImageResource(R.drawable.add_image_icon);
        } else {
            title.setText(currentGame.getTitle());
            gameText.setText(currentGame.getText());
            picture.setImageURI(currentGame.getPicture());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gameCreationImage:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
            case R.id.gameSlideDone:
                if (currentGame == null) {
                    currentGame = new TriviaGame();
                }
                currentGame.setCreationMode(true);
                currentGame.setTitle(title.getText().toString());
                currentGame.setText(gameText.getText().toString());
                currentGame.setPicture(pictureUri);
                sendMessage(view);
                break;
        }
    }

    public void sendMessage(View view) {
        Intent nextActivity = new Intent(this, RoundListActivity.class); // add the activity class you're going to, also uncomment duh.
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putString(IntentKeys.GOOGLE, googleToken);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    public void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            googleToken = data.getString(IntentKeys.GOOGLE);
            currentGame = data.getParcelable(IntentKeys.GAME);
        }
    }
}