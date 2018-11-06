//package me.lgbt.pubhub.chat;
//
//import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.widget.TextView;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import me.lgbt.pubhub.R;
//
//import static me.lgbt.pubhub.R.*;
//
//public class MessageListActivity extends AppCompatActivity {
//
//    private RecyclerView messageRecycler;
//    private MessageListAdapter messageAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(layout.activity_message_list);
//
//        messageRecycler = (RecyclerView) findViewById(id.reyclerview_message_list);
//        messageAdapter = new MessageListAdapter(this, MessageListAdapter.getMessageList());
//        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
//        messageRecycler.setAdapter(messageAdapter);
//    }
//
//
//}