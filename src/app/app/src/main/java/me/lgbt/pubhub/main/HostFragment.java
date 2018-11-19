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
import me.lgbt.pubhub.trivia.utils.interfaces.HostListener;
import me.lgbt.pubhub.trivia.utils.TriviaMessage;

/*
 * ```{
    "messageType": "HostServerMessage",
    "payload": {
        "messageType": "openGame",
        "payload": {
            "gameId": <id>
        }
    }
}```
 */

public class HostFragment extends Fragment implements View.OnClickListener {
    public static final int NEXT = 1;
    public static final int PREVIOUS = -1;
    public static final int START = 0;
    private TextView title;
    private TextView text;
    private ImageView image;
    private FloatingActionButton next;
    private FloatingActionButton back;
    private HostListener passer;
    private View fade;
    private Button launchGame;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();

        assert act != null;
        title = act.findViewById(R.id.slideTitlePlay);
        text = act.findViewById(R.id.slidePlayText);
        image = act.findViewById(R.id.slidePlayPicture);
        next = act.findViewById(R.id.hostNextButton);
        back = act.findViewById(R.id.hostPrevButton);
        fade = act.findViewById(R.id.fadeBackgroundHost);
        launchGame = act.findViewById(R.id.launchGameHost);

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

    @Override
    public void onClick(View view) {
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
                back.show();
                next.show();
                launchGame.setVisibility(View.GONE);
                passer.slideNavClicked(START);
                break;
            }
        }
    }


}
