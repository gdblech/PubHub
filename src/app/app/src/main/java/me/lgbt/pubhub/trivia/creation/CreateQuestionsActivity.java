package me.lgbt.pubhub.trivia.creation;

/**
 * @author Geoffrey Blech
 * Activity for Creating Questions for games of trivia
 * @since 10/13/2018
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.trivia.utils.TriviaGame;
import me.lgbt.pubhub.trivia.utils.TriviaQuestion;
import me.lgbt.pubhub.trivia.utils.TriviaRound;

public class CreateQuestionsActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PICK_IMAGE = 125;
    private String phbToken;
    private TriviaGame currentGame;
    private TriviaRound currentRound;
    private TriviaQuestion currentQuestion;
    private Uri pictureUri;
    private EditText title;
    private EditText text;
    private EditText answer;
    private ImageView picture;
    private FloatingActionButton doneButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_questions);
        title = findViewById(R.id.questionTitle);
        text = findViewById(R.id.questionText);
        answer = findViewById(R.id.answerText);
        picture = findViewById(R.id.questionCreationImage);
        doneButton = findViewById(R.id.questionDoneButton);

        unPack();
        questionSetUp();

        picture.setOnClickListener(this);
        doneButton.setOnClickListener(this);
    }

    public void sendMessage(View view) {
        Intent nextActivity = new Intent(this, QuestionListActivity.class); // add the activity class you're going to, also uncomment duh.
        Bundle extras = new Bundle();

        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putParcelable(IntentKeys.ROUND, currentRound);
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
            currentRound = data.getParcelable(IntentKeys.ROUND);
        }
    }

    public void questionSetUp() {
        if (currentQuestion == null) {
            currentQuestion = new TriviaQuestion();
            picture.setImageResource(R.drawable.add_image_icon);
        } else {
            title.setText(currentQuestion.getTitle());
            text.setText(currentQuestion.getText());
            picture.setImageURI(currentQuestion.getPicture());
            answer.setText(currentQuestion.getAnswer());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.questionDoneButton:{
                String gTitle = title.getText().toString();
                String gText = text.getText().toString();
                String gAnswer = answer.getText().toString();
                if(pictureUri == null){
                    Toast.makeText(this, "A Picture is Required", Toast.LENGTH_LONG).show();
                }else if(gTitle.equals("")){
                    Toast.makeText(this, "A Title is Required", Toast.LENGTH_LONG).show();
                }else if(gText.equals("")){
                    Toast.makeText(this, "A Text is Required", Toast.LENGTH_LONG).show();
                }else if(gAnswer.equals("")){
                    Toast.makeText(this, "An Answer is Required", Toast.LENGTH_LONG).show();
                } else{
                    currentQuestion.setPicture(pictureUri);
                    currentQuestion.setTitle(gTitle);
                    currentQuestion.setText(gText);
                    currentQuestion.setAnswer(gAnswer);
                    currentRound.addQuestion(currentQuestion);

                    sendMessage(view);
                }
                break;
            }
            case R.id.questionCreationImage:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
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
}
