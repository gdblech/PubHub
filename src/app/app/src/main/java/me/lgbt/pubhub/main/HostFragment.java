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
import me.lgbt.pubhub.Utils.Utils;
import me.lgbt.pubhub.interfaces.HostListener;
import me.lgbt.pubhub.trivia.utils.TriviaMessage;

public class HostFragment extends Fragment implements View.OnClickListener {
    public static final int NEXT = 1;
    public static final int PREVIOUS = -1;
    public static final int START = 0;
    private TextView title;
    private TextView text;
    private TextView answer;
    private TextView counter;
    private ImageView image;
    private View line;
    private FloatingActionButton next;
    private FloatingActionButton back;
    private HostListener passer;
    private View fade;
    private Button launchGame;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();

        assert act != null;
        title = act.findViewById(R.id.slideTitleHost);
        text = act.findViewById(R.id.slideTextHost);
        image = act.findViewById(R.id.slidePictureHost);
        next = act.findViewById(R.id.hostNextButton);
        back = act.findViewById(R.id.hostPrevButton);
        fade = act.findViewById(R.id.fadeBackgroundHost);
        launchGame = act.findViewById(R.id.launchGameHost);
        answer = act.findViewById(R.id.slideHostTextAnswer);
        counter = act.findViewById(R.id.hostyOutOfxText);
        line = act.findViewById(R.id.viewHostLine);


        fade.setVisibility(View.VISIBLE);
        fade.bringToFront();
        back.setOnClickListener(this);
        next.setOnClickListener(this);
        next.hide();
        back.hide();
        launchGame.setOnClickListener(this);
    }

    public void startGame() {
        fade.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        passer = (HostListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hosting, container, false);
    }

    public void setSlide(TriviaMessage msg) {
        title.setText(msg.getTitle());
        text.setText(msg.getText());
        image.setImageBitmap(msg.getImage());
    }

    public void switchMode(boolean grading){
        if(!grading){
            answer.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
        }else{
            answer.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }
    }

    public void updateCounter(int x, int y){
        String blackJackAndHookers = x + " out of " + y + " players finished";
        counter.setText(blackJackAndHookers);
    }

    public void setSlide(TriviaMessage msg, String answer) {
        title.setText(msg.getTitle());
        text.setText(msg.getText());
        image.setImageBitmap(msg.getImage());
        this.answer.setText(answer);
    }
    @Override
    public void onClick(View view) {
        //Utils.hideKeyboard(view);
        switch (view.getId()) {
            case R.id.hostPrevButton: {
                passer.slideNavClicked(PREVIOUS);
                break;
            }
            case R.id.hostNextButton: {
                passer.slideNavClicked(NEXT);
                break;
            }
            case R.id.launchGameHost: {
                fade.setVisibility(View.GONE);
                // back.show();
                next.show();
                launchGame.setVisibility(View.GONE);
                passer.slideNavClicked(START);
                break;
            }
        }
    }


}
