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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.interfaces.PlayListener;
import me.lgbt.pubhub.trivia.utils.TriviaMessage;

public class PlayFragment extends Fragment implements View.OnClickListener {
    private TextView title;
    private TextView text;
    private ImageView picture;
    private TextView currentRound;
    private TextView currentQuestion;
    private FloatingActionButton submitAnswer;
    private EditText answer;
    private PlayListener passer;
    private View fade;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // everyone connected gets game info, including host
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();

        assert act != null;
        title = act.findViewById(R.id.slideTitlePlay);
        text = act.findViewById(R.id.slidePlayText);
        picture = act.findViewById(R.id.slidePlayPicture);
        currentRound = act.findViewById(R.id.currentRound);
        currentQuestion = act.findViewById(R.id.currentQuestion);
        submitAnswer = act.findViewById(R.id.doneSlideButton);
        answer = act.findViewById(R.id.slidePlayAnswer);
        fade = act.findViewById(R.id.fadeBackground);

        fade.setVisibility(View.VISIBLE);
        fade.bringToFront();
        submitAnswer.setOnClickListener(this);
        answer.setVisibility(View.GONE);
        submitAnswer.hide();
    }

    public void startGame() {
        fade.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        passer = (PlayListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playing, container, false);
    }

    public void setSlide(TriviaMessage msg) {
        title.setText(msg.getTitle());
        text.setText(msg.getText());
        picture.setImageBitmap(msg.getImage());
        if (msg.isQuestion()) {
            submitAnswer.show();
            answer.setVisibility(View.VISIBLE);
        }else {
            submitAnswer.hide();
            answer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        closeKeyboard();
        switch (view.getId()) {
            case R.id.doneSlideButton: {
                passer.answerClicked(answer.getText().toString());
                answer.setVisibility(View.GONE);
                answer.setText("");

                submitAnswer.hide();
            }
        }
    }

    private void closeKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);        }
    }
}
