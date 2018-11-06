package me.lgbt.pubhub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import me.lgbt.pubhub.chat.Message;
import me.lgbt.pubhub.chat.MessageAdapter;
import me.lgbt.pubhub.chat.UserMessage;

public class TestActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activty);
        RecyclerView rec = findViewById(R.id.testRecycler);

        MessageAdapter adapter = new MessageAdapter(testList());
        rec.setAdapter(adapter);
        rec.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<UserMessage> testList(){
        ArrayList<UserMessage> list = new ArrayList<>();

        list.add(new UserMessage("Hello, world", "Jimmy", 1541480825));
        list.add(new UserMessage("Suck it, world", "Jimmy", 1541580825));
        list.add(new UserMessage("Hello, world", "Kyle", 1541450925));
        list.add(new UserMessage("Nothing is here", "Nothing", 1541680825));

        return list;
    }
}
