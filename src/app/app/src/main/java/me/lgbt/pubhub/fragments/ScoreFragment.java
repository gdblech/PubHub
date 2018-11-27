package me.lgbt.pubhub.fragments;

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
import java.util.Collections;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.Utils.ScoreObject;
import me.lgbt.pubhub.Utils.ScoresAdapter;
import me.lgbt.pubhub.Utils.scoreComparator;

public class ScoreFragment extends Fragment {
    private ArrayList<ScoreObject> teams = new ArrayList<>();
    private RecyclerView recycler;
    private ScoresAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();

        assert act != null;
        recycler = act.findViewById(R.id.scoreFragmentRec);
        adapter = new ScoresAdapter(teams);

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(act));
    }

    public void addTeam(ScoreObject team){
        teams.add(team);
        adapter.notifyItemInserted(teams.size()-1);
    }

    public void setTeams(ArrayList<ScoreObject> teams) {
        this.teams = teams;
        adapter.notifyDataSetChanged();
    }

    public void sortTeams(){
        Collections.sort(teams, new scoreComparator());
    }

    public ScoreObject returnTopTeam(){
        return teams.get(0);
    }
}
