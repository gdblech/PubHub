package me.lgbt.pubhub.fragments;


import android.app.Activity;
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
import android.widget.TextView;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.interfaces.JoinTeamListener;
import me.lgbt.pubhub.trivia.start.QRCodeScanner;

/**
 * @author Geoffrey Blech
 */

public class JoinTeam extends Fragment implements View.OnClickListener {
    private final int REQ_CODE = 12359;
    private JoinTeamListener passer;
    private TextView text;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_team, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();

        assert act != null;
        Button scanQR = act.findViewById(R.id.joinTeamButton);
        scanQR.setOnClickListener(this);
        text = act.findViewById(R.id.joinTeamText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        passer = (JoinTeamListener) context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                passer.qrCodeScanned(data.getStringExtra(IntentKeys.TEAM));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //do nothing, they need to be on a team.
            }
        }
    }

    public void upDateText(String text) {
        this.text.setText(text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.joinTeamButton: {
                scanQR();
                break;
            }
        }
    }

    /**
     * Starts the QR code scanning activity
     */
    public void scanQR() {
        // start QRCodeScanner activity
        Intent scanner = new Intent(getActivity(), QRCodeScanner.class);
        startActivityForResult(scanner, REQ_CODE);
    }
}
