package me.lgbt.pubhub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.Utils.Keyboard;
import me.lgbt.pubhub.interfaces.GradingListener;
import me.lgbt.pubhub.trivia.utils.Answer;
import me.lgbt.pubhub.trivia.utils.TriviaMessage;

/**
 * @author Geoffrey Blech
 */

public class GradingFragment extends Fragment implements View.OnClickListener {
    int answerTracker = 0;
    private TextView title;
    private TextView text;
    private TextView answerGiven;
    private TextView teamAnswer;
    private ImageView image;
    private FloatingActionButton right;
    private FloatingActionButton wrong;
    private GradingListener passer;
    private Answer[] answers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grading, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity act = getActivity();

        assert act != null;
        title = act.findViewById(R.id.gradingTitle);
        text = act.findViewById(R.id.gradingText);
        answerGiven = act.findViewById(R.id.gradingAnswer);
        teamAnswer = act.findViewById(R.id.answer2grade);
        image = act.findViewById(R.id.gradingPicture);
        right = act.findViewById(R.id.answerCorrect);
        wrong = act.findViewById(R.id.answerWrong);
        right.setOnClickListener(this);
        wrong.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        passer = (GradingListener) context;
    }

    public void updateUI(TriviaMessage msg, String answerGiven) {
        title.setText(msg.getTitle());
        text.setText(msg.getText());
        image.setImageBitmap(msg.getImage());
        this.answerGiven.setText(answerGiven);
    }

    public void updateAnswer(String answer) {
        teamAnswer.setText(answer);
    }

    public void answerList(Answer[] answers) {
        this.answers = answers;
        if(answers.length != 0) {
            teamAnswer.setText(answers[answerTracker].getAnswer());
            showButtons();
        }else{
            teamAnswer.setText("");
            right.hide();
            wrong.hide();
        }
    }

    public void showButtons() {
        right.show();
        wrong.show();
    }

    @Override
    public void onClick(View view) {
        Keyboard.hideKeyboard(view);
        switch (view.getId()) {
            case R.id.answerWrong:
                answers[answerTracker].setCorrect(false);
                break;
            case R.id.answerCorrect:
                answers[answerTracker].setCorrect(true);
                break;
        }
        answerTracker++;
        if (answerTracker < answers.length) {
            teamAnswer.setText(answers[answerTracker].getAnswer());
        } else {
            teamAnswer.setText("");
            answerTracker = 0;
            right.hide();
            wrong.hide();
            passer.answerGraded(answers);
        }
    }
}
