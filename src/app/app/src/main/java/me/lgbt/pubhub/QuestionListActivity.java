package me.lgbt.pubhub;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.trivia.QuestionAdapter;
import me.lgbt.pubhub.trivia.TriviaGame;
import me.lgbt.pubhub.trivia.TriviaQuestion;
import me.lgbt.pubhub.trivia.TriviaRound;

public class QuestionListActivity extends AppCompatActivity implements View.OnClickListener{
    private String phbToken;
    private String googleToken;
    private TriviaGame currentGame;
    private TriviaRound currentRound;
    private TriviaQuestion selectedQuestion;
    private RecyclerView questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        FloatingActionButton addQuestion = findViewById(R.id.addQuestionButton);
        questionList = findViewById(R.id.questionList);

        unPack();

        QuestionAdapter adapter = new QuestionAdapter(currentRound.getQuestions());
        questionList.setAdapter(adapter);
        questionList.setLayoutManager(new LinearLayoutManager(this));

        addQuestion.setOnClickListener(this);
    }

    public void sendMessage(View view) {
        Intent nextActivity = new Intent(this, CreateQuestionsActivity.class); // add the activity class you're going to, also uncomment duh.
        Bundle extras = new Bundle();

        extras.putString(IntentKeys.PUBHUB, phbToken);
        extras.putString(IntentKeys.GOOGLE, googleToken);
        extras.putParcelable(IntentKeys.ROUND, currentRound);
        extras.putParcelable(IntentKeys.GAME, currentGame);
        if(selectedQuestion != null){
            extras.putParcelable(IntentKeys.QUESTION, selectedQuestion);
        }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addQuestionButton:
                sendMessage(view);
                break;
        }
    }
}
