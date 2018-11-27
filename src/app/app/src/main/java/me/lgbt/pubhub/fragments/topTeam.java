package me.lgbt.pubhub.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.Utils.ScoreObject;

public class TopTeam extends Fragment {
    private TextView textView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_team, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();
        assert act != null;
        textView = act.findViewById(R.id.topTeampText);
    }

    public void setNumberOneTeam(ScoreObject scoreObject){
        textView.setText(scoreObject.getTeamName());

    }



}
