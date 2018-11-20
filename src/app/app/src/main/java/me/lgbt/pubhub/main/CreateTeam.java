package me.lgbt.pubhub.main;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.interfaces.TeamNameCreatedListenser;


public class CreateTeam extends Fragment implements View.OnClickListener{
    private Button btnTeamCreate;
    private EditText text;
    private String name = "";
    private TeamNameCreatedListenser passer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return   inflater.inflate(R.layout.fragment_create_team, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        passer = (TeamNameCreatedListenser) context;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();

        assert act != null;
        btnTeamCreate = act.findViewById(R.id.createTeamButton);
        text = act.findViewById(R.id.createTeamText);
        btnTeamCreate.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createTeamButton:
                name = text.getText().toString();
                if(name.equals("")){
                    Toast.makeText(getActivity(), "A Team Name Is Required", Toast.LENGTH_LONG).show();
                }else {
                    passer.nameChosen(name);
                }
                break;
        }
    }
}
