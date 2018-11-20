package me.lgbt.pubhub.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;

import static android.content.Intent.getIntent;


public class WaitingOpenFragment extends Fragment implements View.OnClickListener {
    private final int REQ_CODE = 12359;
    private String phbToken;
    TextView message;
    private String welcomeMesg;
    private Button btnToJoinTeam;
    private boolean isGame;
    private boolean isInTeam;

    public WaitingOpenFragment(){
        //Empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_waiting_open, container, false);
        btnToJoinTeam =  v.findViewById(R.id.btnToJointeam);
        message = v.findViewById(R.id.waitingOpenText);
        welcomeMesg = welcomeMeg();
        System.out.println(welcomeMesg);

        //if there is game scan QR_code to join a table
        isGame = isGameAvail();
        isInTeam = isInTeam();

        if(isGame){//if game is available
            if(isInTeam) { //and is in a team
                try {
                    waitStartGame(); //go to wait for start
                } catch (Exception e) {
                    //catch what
                }
            } else{
                System.out.println(welcomeMesg);
                message.setText(welcomeMesg);
                btnToJoinTeam.setVisibility(View.VISIBLE);
                btnToJoinTeam.setOnClickListener(this);
            }

        } else {
            System.out.println(welcomeMesg);
            message.setText(welcomeMesg);
            btnToJoinTeam.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();
        assert act != null;
        TextView text = act.findViewById(R.id.waitingOpenText);
    }

    private void changeFrangment(){ //change current frangment to join team
        System.out.println("Trying to join Team");
        getFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, new JoinTeam())
                .commit();

    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnToJointeam:
                try {
                    changeFrangment();
                }
                catch (Exception e){
                    //catch what
                }
                break;
        }
    }

    //if game available
    //if comes from the messages from the server
    public boolean isGameAvail(){
        isGame = true;
        //todo add code to set isGame
        return isGame;
    }

    public String welcomeMeg(){
        if (isGameAvail()){
            welcomeMesg = "welcome to PubHub!";
            }
            else{
            welcomeMesg = " No game available! Please check Back later";
        }
        return welcomeMesg;
    }

    public boolean isInTeam(){
        isInTeam = false;
        //todo add code to set isInteam;
        return isInTeam;
    }

    public void waitStartGame(){
        //go to wait for start game
        Intent waitStartGame = new Intent(getActivity(), WaitForStart.class);
        startActivity(waitStartGame);

    }

    public void toTeamJoin(){
        System.out.println("Trying to join Team");
        Intent toTeamJoin = new Intent(getActivity(), JoinTeam.class);
        Bundle extras = new Bundle();
        //extras.putString(IntentKeys.PUBHUB, phbToken);
        //toTeamJoin.putExtras(extras);
        startActivity(toTeamJoin);
    }

}
