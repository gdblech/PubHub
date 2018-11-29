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
import me.lgbt.pubhub.Utils.teamScoreObject;
import me.lgbt.pubhub.Utils.teamScoreAdapter;
import me.lgbt.pubhub.Utils.teamScoreComparator;

/**
 * @author Geoffrey Blech
 */

public class ScoreFragment extends Fragment {
    private ArrayList<teamScoreObject> teams = new ArrayList<>();
    private teamScoreAdapter adapter;

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
        RecyclerView recycler = act.findViewById(R.id.scoreFragmentRec);
        adapter = new teamScoreAdapter(teams);

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(act));
    }

    public void addTeam(teamScoreObject team) {
        teams.add(team);
        adapter.notifyItemInserted(teams.size() - 1);
    }

    public void setTeams(ArrayList<teamScoreObject> teams) {
        this.teams.addAll(teams);
    }

    public void sortTeams() {
        Collections.sort(teams, new teamScoreComparator());
        adapter.notifyDataSetChanged();
    }

    public teamScoreObject returnTopTeam() {
        return teams.get(0);
    }
}
