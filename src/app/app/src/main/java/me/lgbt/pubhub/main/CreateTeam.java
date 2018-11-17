package me.lgbt.pubhub.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.lgbt.pubhub.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTeam extends Fragment {


    public CreateTeam() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_team, container, false);
    }

}
