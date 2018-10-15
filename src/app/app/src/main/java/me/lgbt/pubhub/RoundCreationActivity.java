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
import me.lgbt.pubhub.trivia.TriviaRound;

public class RoundCreationActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int PICK_IMAGE = 125;
    private String phbToken;
    private String googleToken;
    private Uri pictureUri;
    private EditText title;
    private EditText text;
    private ImageView picture;
    private TriviaRound currentRound;
    private TriviaGame currentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_creation);
        title = findViewById(R.id.roundTitle);
        text = findViewById(R.id.roundText);
        picture = findViewById(R.id.roundCreationImage);
        FloatingActionButton doneButton = findViewById(R.id.roundDoneButton);

        unPack();
        roundSetUp();

        picture.setOnClickListener(this);
        doneButton.setOnClickListener(this);
    }

    void roundSetUp(){
        if (currentRound == null){
            currentRound = new TriviaRound();
            picture.setImageResource(R.drawable.add_image_icon);
        }else{
            title.setText(currentRound.getTitle());
            text.setText(currentRound.getText());
            picture.setImageURI(currentRound.getPicture());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pictureUri = data.getData();
            picture.setImageURI(pictureUri);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.roundDoneButton:
                currentRound.setPicture(pictureUri);
                currentRound.setTitle(title.getText().toString());
                currentRound.setText(text.getText().toString());
                currentRound.setCreationMode(true);
                sendMessage(view);
                break;
            case R.id.roundCreationImage:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
        }
    }

    public void sendMessage(View view) {
        Intent nextActivity = new Intent(this,  QuestionListActivity.class); // add the activity class you're going to, also uncomment duh.
        Bundle extras = new Bundle();

        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putString(IntentKeys.GOOGLE, googleToken);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        extras.putParcelable(IntentKeys.ROUND, currentRound);

        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    public void  unPack(){
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            googleToken = data.getString(IntentKeys.GOOGLE);
            currentGame = data.getParcelable(IntentKeys.GAME);
            currentRound = data.getParcelable(IntentKeys.ROUND);
        }
    }
}
