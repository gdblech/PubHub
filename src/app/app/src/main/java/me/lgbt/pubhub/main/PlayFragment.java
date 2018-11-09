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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.trivia.utils.PlayListener;
import me.lgbt.pubhub.trivia.utils.TriviaMessage;

public class PlayFragment extends Fragment implements View.OnClickListener {
    public final int NEXT = 1;
    public final int PREVIOUS = -1;
    public final int START = 0;
    private TextView title;
    private TextView text;
    private ImageView picture;
    private FloatingActionButton doneOrNext;
    private FloatingActionButton back;
    private EditText answer;
    private boolean host = false;
    private PlayListener passer;
    private View fade;
    private Button launchGame;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity act = getActivity();
        title = act.findViewById(R.id.slideTitlePlay);
        text = act.findViewById(R.id.slidePlayText);
        picture = act.findViewById(R.id.slidePlayPicture);
        doneOrNext = act.findViewById(R.id.doneSlideButton);
        back = act.findViewById(R.id.prevSlideButton);
        answer = act.findViewById(R.id.slidePlayAnswer);
        fade = act.findViewById(R.id.fadeBackground);
        launchGame = act.findViewById(R.id.launch_game);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fade.setVisibility(View.VISIBLE);
        fade.bringToFront();
        back.setOnClickListener(this);
        doneOrNext.setOnClickListener(this);
        answer.setVisibility(View.GONE);
        doneOrNext.hide();
        launchGame.setOnClickListener(this);
    }

    public void startGame(){
        fade.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        passer = (PlayListener) context;
    }

    public void hostMode() {
        answer.setVisibility(View.GONE);
        launchGame.setVisibility(View.VISIBLE);
        host = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playing, container, false);
    }

    public void setSlide(TriviaMessage msg, boolean Question) {
        title.setText(msg.getTitle());
        text.setText(msg.getText());
        picture.setImageURI(msg.getPicture());
        if(Question){
            doneOrNext.show();
            answer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prevSlideButton: {
                passer.slideNavClicked(PREVIOUS);
            }
            case R.id.doneSlideButton: {
                if (host) {
                    passer.slideNavClicked(NEXT);
                } else {
                    passer.answerClicked(answer.getText().toString());
                }
            }
            case R.id.launch_game:{
                fade.setVisibility(View.GONE);
                back.show();
                launchGame.setVisibility(View.VISIBLE);
                doneOrNext.setImageResource(R.drawable.ic_baseline_navigate_next_24px);
                passer.slideNavClicked(START);
            }
        }
    }
}
