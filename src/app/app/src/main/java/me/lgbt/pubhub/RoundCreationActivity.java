package me.lgbt.pubhub;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RoundCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_creation);
        TextView title = findViewById(R.id.roundTitle);
        TextView text = findViewById(R.id.roundText);
        ImageView image = findViewById(R.id.roundCreationImage);
        FloatingActionButton doneButton = findViewById(R.id.roundButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view);
            }
        });
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, RoundListActivity.class); // add the activity class you're going to, also uncomment duh.

        startActivity(intent);
    }
}
