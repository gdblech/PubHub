package me.lgbt.pubhub.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.Utils.ScoreObject;
import me.lgbt.pubhub.interfaces.HostListener;

public class TopTeam extends Fragment implements View.OnClickListener{
    private TextView textView;
    private FloatingActionButton next;
    private HostListener passer;
    public static final int NEXT = 1;

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
        next = act.findViewById(R.id.topNextButton);
        next.setOnClickListener(this);
    }

    public void setNumberOneTeam(ScoreObject scoreObject){
        textView.setText(scoreObject.getTeamName());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        passer = (HostListener) context;
    }

    public void hosting(){
        next.show();
    }


    @Override
    public void onClick(View view) {
        passer.slideNavClicked(NEXT);
    }
}
