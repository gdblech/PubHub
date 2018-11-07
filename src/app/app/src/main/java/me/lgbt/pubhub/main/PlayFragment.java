package me.lgbt.pubhub.main;

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
import me.lgbt.pubhub.trivia.utils.TriviaMessage;

public class PlayFragment extends Fragment {
    TextView title;
    TextView text;
    ImageView picture;
    FloatingActionButton doneOrNext;
    FloatingActionButton back;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();
        title = act.findViewById(R.id.slideTitlePlay);
        text = act.findViewById(R.id.slidePlayText);
        picture = act.findViewById(R.id.slidePlayPicture);
        doneOrNext = act.findViewById(R.id.doneSlideButton);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playing, container, false);
    }

    public void setSlide(TriviaMessage msg){
        //todo set fields from msg
    }
}
