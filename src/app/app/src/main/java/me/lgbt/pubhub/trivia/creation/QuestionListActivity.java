package me.lgbt.pubhub.trivia.creation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.interfaces.ClickListener;
import me.lgbt.pubhub.trivia.utils.QuestionAdapter;
import me.lgbt.pubhub.trivia.utils.TriviaGame;
import me.lgbt.pubhub.trivia.utils.TriviaQuestion;
import me.lgbt.pubhub.trivia.utils.TriviaRound;

/**
 * @author Geoffrey Blech
 */
public class QuestionListActivity extends AppCompatActivity implements View.OnClickListener, ClickListener {
    private String phbToken;
    private TriviaGame currentGame;
    private TriviaRound currentRound;
    private TriviaQuestion selectedQuestion;
    private int questionPosition;
    private int roundPosition;
    private QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        FloatingActionButton addQuestion = findViewById(R.id.addQuestionButton);
        FloatingActionButton doneQuestion = findViewById(R.id.quenstionListDoneButton);
        final RecyclerView questionList = findViewById(R.id.questionList);

        unPack();

        adapter = new QuestionAdapter(currentRound.getQuestions(), this);
        questionList.setAdapter(adapter);

        questionList.setLayoutManager(new LinearLayoutManager(this));

        addQuestion.setOnClickListener(this);
        doneQuestion.setOnClickListener(this);
    }

    private void sendMessage() {
        Intent nextActivity = new Intent(this, CreateQuestionsActivity.class);
        Bundle extras = new Bundle();

        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putParcelable(IntentKeys.ROUND, currentRound);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        extras.putInt(IntentKeys.RPOSITION, roundPosition);
        if (selectedQuestion != null) {
            extras.putParcelable(IntentKeys.QUESTION, selectedQuestion);
            extras.putInt(IntentKeys.QPOSITION, questionPosition);
        } else {
            extras.putInt(IntentKeys.QPOSITION, -1);
        }

        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    private void doneMessage() {
        Intent doneActivity = new Intent(this, RoundListActivity.class);
        Bundle extras = new Bundle();

        currentRound.trimQuestions();
        if (roundPosition == -1) {
            currentGame.addRound(currentRound);
        } else {
            currentGame.replaceRound(roundPosition, currentRound);
        }

        extras.putParcelable(IntentKeys.GAME, currentGame);
        extras.putString(IntentKeys.PUBHUB, phbToken);
        doneActivity.putExtras(extras);

        startActivity(doneActivity);
        finish();
    }

    private void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            currentGame = data.getParcelable(IntentKeys.GAME);
            currentRound = data.getParcelable(IntentKeys.ROUND);
            roundPosition = data.getInt(IntentKeys.RPOSITION);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addQuestionButton:
                sendMessage();
                break;
            case R.id.quenstionListDoneButton:
                doneMessage();
                break;
        }
    }

    @Override
    public void onPositionClicked(int position, int button) {
        switch (button) {
            case R.id.editButton:
                selectedQuestion = currentRound.getQuestions().get(position);
                questionPosition = position;
                sendMessage();
                break;
            case R.id.deleteButton:
                currentRound.removeQuestion(position);
                adapter.notifyItemRemoved(position);
                break;
        }
    }
}
