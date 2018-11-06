package me.lgbt.pubhub.trivia.creation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.trivia.utils.TriviaGame;

public class GameSlideCreationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_IMAGE = 125;
    private String phbToken;
    private Uri pictureUri;
    private TriviaGame currentGame;
    private EditText title;
    private EditText text;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.slide_creation);
        title = findViewById(R.id.slideCreateTitle);
        text = findViewById(R.id.slideCreateText1);
        picture = findViewById(R.id.slideCreateImage);
        FloatingActionButton doneButton = findViewById(R.id.questionDoneButton);


        title.setHint(R.string.enter_game_title);
        text.setHint(R.string.enter_game_text);

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
            text.setText(currentGame.getText());
            picture.setImageURI(currentGame.getPicture());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.slideCreateImage:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
            case R.id.questionDoneButton: {
                String gTitle = title.getText().toString();
                String gText = text.getText().toString();
                if (pictureUri == null) {
                    Toast.makeText(this, "A Picture is Required", Toast.LENGTH_LONG).show();
                } else if (gTitle.equals("")) {
                    Toast.makeText(this, "A Title is Required", Toast.LENGTH_LONG).show();
                } else if (gText.equals("")) {
                    Toast.makeText(this, "A Text is Required", Toast.LENGTH_LONG).show();
                } else {
                    currentGame.setPicture(pictureUri);
                    currentGame.setTitle(gTitle);
                    currentGame.setText(gText);
                    currentGame.setCreationMode(true);
                    sendMessage();
                }
                break;
            }
        }
    }

    public void sendMessage() {
        Intent nextActivity = new Intent(this, RoundListActivity.class); // add the activity class you're going to, also uncomment duh.
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    public void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            currentGame = data.getParcelable(IntentKeys.GAME);
        }
    }
}