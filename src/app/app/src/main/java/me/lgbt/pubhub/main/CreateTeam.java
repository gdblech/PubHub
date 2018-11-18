package me.lgbt.pubhub.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.lgbt.pubhub.R;


public class CreateTeam extends Fragment{
    private Button btnTeamCreate;


    public CreateTeam() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_create_team, container, false);
        btnTeamCreate = v.findViewById(R.id.btnTeamCreate);
        btnTeamCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTeam();
            }
        });

        return v;
    }

    public void createTeam(){
        //todo create team
    }




}
