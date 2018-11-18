package me.lgbt.pubhub.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.trivia.utils.interfaces.ClickListener;

public class TeamAnswerFragment extends Fragment  implements ClickListener {
    private ArrayList<String> answers = new ArrayList<>();
    private RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_answer_team, container, false);
    }

    @Override
    public void onPositionClicked(int position, int button) {

    }
}
