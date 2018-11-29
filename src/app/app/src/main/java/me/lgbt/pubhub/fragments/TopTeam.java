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
import me.lgbt.pubhub.Utils.teamScoreObject;
import me.lgbt.pubhub.interfaces.HostListener;

/**
 * @author Linh Tran
 * @author Geoffrey Blech
 *
 */

public class TopTeam extends Fragment implements View.OnClickListener {
    public static final int NEXT = 1;
    private TextView textView;
    private FloatingActionButton next;
    private HostListener passer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    public void setNumberOneTeam(teamScoreObject teamScoreObject) {
        textView.setText(teamScoreObject.getTeamName());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        passer = (HostListener) context;
    }

    /**
     * called only by the host, this shows a FAB allowing them to move onto the next slide.
     */
    public void hosting() {
        next.show();
    }


    @Override
    public void onClick(View view) {
        passer.slideNavClicked(HostFragment.NEXT);
    }
}
