package me.lgbt.pubhub;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import me.lgbt.pubhub.Trivia.TriviaGame;

public class GameSlideCreationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_IMAGE = 125;
    private String phbToken;
    private Uri pictureUri;
    private Bundle data;
    private TriviaGame currentGame;
    private EditText title;
    private EditText gameText;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_slide_creation);

        picture = findViewById(R.id.gameCreationImage);
        FloatingActionButton doneButton = findViewById(R.id.gameSlideDone);
        title = findViewById(R.id.gameTitle);
        gameText = findViewById(R.id.gameSlideText);
        picture.setImageResource(R.drawable.add_image_icon);

        data = getIntent().getExtras();

        try {
            phbToken = data.getString("TOKEN");
        }catch (NullPointerException e){
            phbToken = "No Token Passed";
        }


        System.out.println(phbToken);
        picture.setOnClickListener(this);
        doneButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpGame();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setUpGame();
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, RoundListActivity.class);
        intent.putExtra("TOKEN", phbToken);
        intent.putExtra("TRIVIAGAME", currentGame);
        startActivity(intent);
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
        currentGame = data.getParcelable("TRIVIAGAME");
        if (currentGame == null) {
            currentGame = new TriviaGame();
        }
//        if (currentGame.getPicture() == null) {
//            picture.setImageResource(R.drawable.add_image_icon);
//        } else {
//            picture.setImageURI(currentGame.getPicture());
//        }
        if (currentGame.getText() != null) {
            title.setText(currentGame.getText());
        }
        if (currentGame.getTitle() != null) {
            title.setText(currentGame.getTitle());
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.gameCreationImage:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
            case R.id.gameSlideDone:
                if(currentGame == null){
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
}
