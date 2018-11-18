package me.lgbt.pubhub.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.trivia.start.QRCodeScanner;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

public class JoinTeam extends Fragment implements View.OnClickListener {
    private final int REQ_CODE = 12359;
    private String QR;
    private Button btnToTeamCreate;
    private Button btnToQR;
    private Button btnTeamJoin;
    private boolean isTeam;
    private boolean isTeamFull;
    private String mesg;
    TextView disMesg;


    public JoinTeam() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_join_team, container, false);
        disMesg = v.findViewById(R.id.joinTeamText);
        btnTeamJoin = v.findViewById(R.id.btnTeamJoin);
        btnToQR = v.findViewById(R.id.btnToQR);
        btnToTeamCreate = v.findViewById(R.id.btnToCreateTeam);

        isTeam = isTeam(mesg);
        isTeamFull = isTeamFull(mesg);

        //start by scanning QR code
        if(QR == null){//QR code has not been scanned
            btnToQR.setVisibility(View.VISIBLE);
            btnTeamJoin.setVisibility(View.GONE);
            btnToTeamCreate.setVisibility(View.GONE);
            disMesg.setText("Scan QRcode");
        }else if (isTeam){//there is a team for the table
            if(!isTeamFull) { //and the team is not full
                btnTeamJoin.setVisibility(View.VISIBLE);
                btnToQR.setVisibility(View.GONE);
                btnToTeamCreate.setVisibility(View.GONE);
                disMesg.setText("Join Team");
            }else{
                btnTeamJoin.setVisibility(View.VISIBLE);
                btnToQR.setVisibility(View.GONE);
                btnToTeamCreate.setVisibility(View.GONE);
                disMesg.setText("Create Team");
            }
        }

        return v;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnToQR:{
                scanQR();
                break;
            }
            case R.id.btnTeamJoin:{
                teamJoin();
                break;
            }
            case R.id.btnToCreateTeam:{
                toTeamCreate();
                break;
            }
        }
    }

    //open qr_code activity
    public void scanQR(){
        // start QRCodeScanner activity
        Intent scanner = new Intent(getActivity(), QRCodeScanner.class);
        startActivityForResult(scanner, REQ_CODE);
        //todo: how does qr code return
    }

    private boolean isTeam(String mesg){
        isTeamFull = false;
        //todo:

        return isTeam;
    }

    private boolean isTeamFull( String mesg){
        isTeam = false;
        //todo:

        return isTeamFull;
    }

    private void teamJoin(){
        //todo send message to server...how?
        String teamJoinJSON = "{\"messageType\":\"PlayerServerMessage\",\"payload\":{\"messageType\":\"JoinTeam\",\"payload\":{\"QRCode\" :\"1539ccd0-e391-11e8-9f32-f2801f1b9fd\"}}}";
    }

    private void toTeamCreate(){
        //start create team
        Intent intent = new Intent(getActivity(), CreateTeam.class);
        startActivity(intent);
    }
}
