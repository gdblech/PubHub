package me.lgbt.pubhub.trivia.creation;

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

/**
 * @author Geoffrey Blech
 * Activity for Creating Questions for games of Trivia
 * @since 10/13/2018
 */
public class CreateQuestionsActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PICK_IMAGE = 125;
    private String phbToken;
    private int questionPosition;
    private int roundPosition;
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
        setContentView(R.layout.slide_creation);
        title = findViewById(R.id.slideCreateTitle);
        text = findViewById(R.id.slideCreateText2);
        answer = findViewById(R.id.slideCreateText1);
        picture = findViewById(R.id.slideCreateImage);
        doneButton = findViewById(R.id.questionDoneButton);

        text.setVisibility(View.VISIBLE);
        title.setHint(R.string.question_title);
        text.setHint(R.string.question_text);
        answer.setHint(R.string.enter_answer);

        unPack();
        questionSetUp();

        picture.setOnClickListener(this);
        doneButton.setOnClickListener(this);
    }

    public void sendMessage() {
        Intent nextActivity = new Intent(this, QuestionListActivity.class); // add the activity class you're going to, also uncomment duh.
        Bundle extras = new Bundle();

        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putParcelable(IntentKeys.ROUND, currentRound);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        extras.putInt(IntentKeys.RPOSITION, roundPosition);


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
            currentQuestion = data.getParcelable(IntentKeys.QUESTION);
            questionPosition = data.getInt(IntentKeys.QPOSITION);
            roundPosition = data.getInt(IntentKeys.RPOSITION);
        }
    }

    public void questionSetUp() {
        if (currentQuestion == null) {
            currentQuestion = new TriviaQuestion();
            picture.setImageResource(R.drawable.add_image_icon);
        } else {
            title.setText(currentQuestion.getTitle());
            text.setText(currentQuestion.getText());
            pictureUri = currentQuestion.getPicture();
            picture.setImageURI(pictureUri);
            answer.setText(currentQuestion.getAnswer());

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.questionDoneButton: {
                String gTitle = title.getText().toString();
                String gText = text.getText().toString();
                String gAnswer = answer.getText().toString();
                if (pictureUri == null) {
                    Toast.makeText(this, "A Picture is Required", Toast.LENGTH_LONG).show();
                } else if (gTitle.equals("")) {
                    Toast.makeText(this, "A Title is Required", Toast.LENGTH_LONG).show();
                } else if (gText.equals("")) {
                    Toast.makeText(this, "A Question is Required", Toast.LENGTH_LONG).show();
                } else if (gAnswer.equals("")) {
                    Toast.makeText(this, "An Answer is Required", Toast.LENGTH_LONG).show();
                } else {
                    currentQuestion.setPicture(pictureUri);
                    currentQuestion.setTitle(gTitle);
                    currentQuestion.setText(gText);
                    currentQuestion.setAnswer(gAnswer);
                    if (questionPosition == -1) {
                        currentRound.addQuestion(currentQuestion);
                    } else {
                        currentRound.replaceQuestion(questionPosition, currentQuestion);
                    }
                    sendMessage();
                }
                break;
            }
            case R.id.slideCreateImage:
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
