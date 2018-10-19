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
import me.lgbt.pubhub.trivia.utils.QuestionAdapter;
import me.lgbt.pubhub.trivia.utils.TriviaGame;
import me.lgbt.pubhub.trivia.utils.TriviaQuestion;
import me.lgbt.pubhub.trivia.utils.TriviaRound;

public class QuestionListActivity extends AppCompatActivity implements View.OnClickListener {
    private String phbToken;
    private TriviaGame currentGame;
    private TriviaRound currentRound;
    private TriviaQuestion selectedQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        FloatingActionButton addQuestion = findViewById(R.id.addQuestionButton);
        FloatingActionButton doneQuestion = findViewById(R.id.quenstionListDoneButton);
        final RecyclerView questionList = findViewById(R.id.questionList);

        unPack();

        QuestionAdapter adapter = new QuestionAdapter(currentRound.getQuestions());
        questionList.setAdapter(adapter);

        questionList.setLayoutManager(new LinearLayoutManager(this));

        addQuestion.setOnClickListener(this);
        doneQuestion.setOnClickListener(this);
    }

    public void sendMessage(View view) {
        Intent nextActivity = new Intent(this, CreateQuestionsActivity.class);
        Bundle extras = new Bundle();

        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putParcelable(IntentKeys.ROUND, currentRound);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        if (selectedQuestion != null) {
            extras.putParcelable(IntentKeys.QUESTION, selectedQuestion);
        }

        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    public void doneMessage(View view) {
        Intent doneActivity = new Intent(this, RoundListActivity.class);
        Bundle extras = new Bundle();

        currentRound.trimQuestions();
        currentGame.addRound(currentRound);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        extras.putString(IntentKeys.PUBHUB, phbToken);
        doneActivity.putExtras(extras);

        startActivity(doneActivity);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addQuestionButton:
                sendMessage(view);
                break;
            case R.id.quenstionListDoneButton:
                doneMessage(view);
                break;
        }
    }
}
