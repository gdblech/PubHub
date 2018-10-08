package me.lgbt.pubhub;

import android.arch.lifecycle.ViewModelStoreOwner;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameSlideCreationActivity extends AppCompatActivity {

    private String phbToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_slide_creation);
        FloatingActionButton doneButton = findViewById(R.id.gameSlideDone);
        TextView title = findViewById(R.id.gameTitle);
        TextView gameText = findViewById(R.id.gameSlideText);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view);
            }
        });
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, RoundListActivity.class); // add the activity class you're going to, also uncomment duh.
        intent.putExtra("TOKEN", phbToken);
        startActivity(intent);
    }
}
