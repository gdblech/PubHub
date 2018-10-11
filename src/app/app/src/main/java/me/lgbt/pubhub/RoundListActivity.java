package me.lgbt.pubhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RoundListActivity extends AppCompatActivity {

    private String phbToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_list);

        Intent origin = getIntent();
        phbToken = origin.getStringExtra("TOKEN");

    }
}
