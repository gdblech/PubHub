package me.lgbt.pubhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

import me.lgbt.pubhub.Trivia.TriviaGame;

public class GameSlideCreationActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 125;
    private String phbToken;
    private Uri pictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_slide_creation);

        final ImageView picture = findViewById(R.id.gameCreationImage);
        FloatingActionButton doneButton = findViewById(R.id.gameSlideDone);
        final EditText title = findViewById(R.id.gameTitle);
        final EditText gameText = findViewById(R.id.gameSlideText);

        Bundle data = getIntent().getExtras();
        phbToken = data.getString("TOKEN");
        final TriviaGame currentGame = collectGame(data);

        if (currentGame.getPicture() == null) {
            picture.setImageResource(R.drawable.add_image_icon);
        } else {
            picture.setImageURI(currentGame.getPicture());
        }
        if (currentGame.getText() != null) {
            title.setText(currentGame.getText());
        }
        if (currentGame.getTitle() != null) {
            title.setText(currentGame.getTitle());
        }

        System.out.println(phbToken);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentGame.setCreationMode(true);
                currentGame.setTitle(title.getText().toString());
                currentGame.setText(gameText.getText().toString());
                currentGame.setPicture(pictureUri);


                sendMessage(view, currentGame);
            }
        });
    }

    public void sendMessage(View view, TriviaGame currentGame) {
        Intent intent = new Intent(this, RoundListActivity.class); // add the activity class you're going to, also uncomment duh.
        intent.putExtra("TOKEN", phbToken);
        intent.putExtra("TRIVIAGAME", currentGame);

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pictureUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pictureUri);

                ImageView imageView = findViewById(R.id.gameCreationImage);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    TriviaGame collectGame(Bundle data) {
        TriviaGame game = data.getParcelable("TRIVIAGAME");
        if (game == null) {
            game = new TriviaGame();
        }
        return game;
    }
}
