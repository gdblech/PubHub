package me.lgbt.pubhub.main;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.interfaces.GradingListener;
import me.lgbt.pubhub.trivia.utils.TriviaMessage;

public class GradingFragment extends Fragment implements View.OnClickListener{
    private TextView title;
    private TextView text;
    private TextView answerGiven;
    private TextView teamAnswer;
    private ImageView image;
    private FloatingActionButton right;
    private FloatingActionButton wrong;
    private GradingListener passer;

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

    public void updateUI(TriviaMessage msg, String answerGiven, String answer ){
        title.setText(msg.getTitle());
        text.setText(msg.getText());
        image.setImageBitmap(msg.getImage());
        this.answerGiven.setText(answerGiven);
        teamAnswer.setText(answer);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.answerWrong:
                passer.answerGraded(false);
                break;
            case R.id.answerCorrect:
                passer.answerGraded(true);
                break;
        }
    }
}
