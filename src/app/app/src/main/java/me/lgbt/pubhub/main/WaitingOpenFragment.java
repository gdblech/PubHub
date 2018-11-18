package me.lgbt.pubhub.main;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.trivia.start.QRCodeScanner;


public class WaitingOpenFragment extends Fragment implements View.OnClickListener {
    TextView message;
    private String welcomeMesg;
    private Button btnToChat;
    private Button btnToQR;
    private boolean isGame;


    public WaitingOpenFragment(){
        //Empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_waiting_open, container, false);
        btnToQR =  v.findViewById(R.id.btnToQR);
        btnToChat =  v.findViewById(R.id.btnToChat);
        message = v.findViewById(R.id.waitingOpenText);
        welcomeMesg = welcomeMeg();
        System.out.println(welcomeMesg);

        //if there is game scan QR_code to join a table
        isGame = isGameAvail();
        if(isGame){
            System.out.println(welcomeMesg);
            btnToChat.setVisibility(View.GONE);
            btnToQR.setVisibility(View.VISIBLE);
            message.setText(welcomeMesg);

        } else {
            System.out.println(welcomeMesg);
            btnToChat.setVisibility(View.VISIBLE);
            btnToQR.setVisibility(View.GONE);
            message.setText(welcomeMesg);
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

    public void onClick(View view){

    }

    //open qr_code activity
    public void openQR(){
        Intent i = new Intent(getActivity(), QRCodeScanner.class);
        startActivity(i);
    }


    //if game available
    //if comes from the messages from the server
    public boolean isGameAvail(){
        isGame = true;
        return isGame;
    }

    public String welcomeMeg(){
        if (isGameAvail()){
            welcomeMesg = " welcome back";
            }
            else{
            welcomeMesg = " No game available!";
        }
        return welcomeMesg;
    }



}
