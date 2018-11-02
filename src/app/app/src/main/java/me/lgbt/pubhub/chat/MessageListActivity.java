//package me.lgbt.pubhub.chat;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import me.lgbt.pubhub.R;
//
//public class MessageListActivity extends AppCompatActivity {
//
//    private RecyclerView messageRecycler;
//    private MessageListAdapter messageAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_message_list);
//
//        messageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
//        messageAdapter = new MessageListAdapter(this, MessageListAdapter.);
//        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
//        //messageRecycler.setAdapter(messageAdapter);
//    }
//}