package me.lgbt.pubhub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.Utils.AnswerAdapter;
import me.lgbt.pubhub.interfaces.ClickListener;
import me.lgbt.pubhub.interfaces.TeamAnswerListener;

/**
 * @author Geoffrey Blech
 */

public class TeamAnswerFragment extends Fragment implements ClickListener {
    private ArrayList<String> answers = new ArrayList<>();
    private RecyclerView recycler;
    private TeamAnswerListener passer;
    private AnswerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_answer_team, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();

        assert act != null;
        recycler = act.findViewById(R.id.teamAnswerRecycler);
        adapter = new AnswerAdapter(answers, this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(act));

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        passer = (TeamAnswerListener) context;
    }

    /**
     * @param position the position in the list that was selected from the recycler
     * @param button   which button was pushed, in this instance only 1 button can be pushed
     */
    @Override
    public void onPositionClicked(int position, int button) {
        passer.teamAnswerChosen(answers.get(position));
    }

    public void addAnswer(String answer) {
        answers.add(answer);
        adapter.notifyItemInserted(answers.size() - 1);
    }

    public void dumpAnswers() {
        answers.clear();
        adapter.notifyDataSetChanged();
    }
}
