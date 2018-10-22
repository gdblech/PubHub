package me.lgbt.pubhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.trivia.creation.GameSlideCreationActivity;

public class TeamSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    final int REQ_CODE = 12359;
    String phbToken;
    String teamID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!sessionCheck()) {
            scanQR();
            if (!teamCheck()) {
                //create team if doesn't exist
            }
        }
    }


    //Return true if they are already on a team, false if they are not.
    private boolean sessionCheck() {
        return false;
    }

    //Returns true if the team already exists, returns false if the team does not exist.
    private boolean teamCheck() {
        return false;
    }


    private void scanQR() {
        //start QRCodeScanner sctivity
        Intent scanner = new Intent(this, QRCodeScanner.class);
        startActivityForResult(scanner, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK && data != null) {
            teamID = data.getStringExtra(IntentKeys.TEAM);
        }
    }

    private void sendMessage(View view) {
        Intent nextActivity = new Intent(this, GameSlideCreationActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);
        nextActivity.putExtras(extras);
        startActivity(nextActivity);
        finish();
    }

    private void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
        }
    }

    @Override
    public void onClick(View view) {

    }
}