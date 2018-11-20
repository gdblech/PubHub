package me.lgbt.pubhub.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import me.lgbt.pubhub.R;
import me.lgbt.pubhub.interfaces.ChatClickListener;
import me.lgbt.pubhub.chat.MessageAdapter;
import me.lgbt.pubhub.chat.UserMessage;

public class ChatFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {

    private ArrayList<UserMessage> messageList;
    private RecyclerView chatBox;
    private FloatingActionButton send;
    private EditText message;
    private ChatClickListener dataPasser;
    private MessageAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageList = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (ChatClickListener) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity act = getActivity();
        assert act != null;
        message = act.findViewById(R.id.chatMessage);
        send = act.findViewById(R.id.sendMessage);
        chatBox = act.findViewById(R.id.chatBox);
        message.setOnKeyListener(this);

        adapter = new MessageAdapter(messageList);
        chatBox.setAdapter(adapter);
        chatBox.setLayoutManager(new LinearLayoutManager(getContext()));
        send.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    public void addMessage(UserMessage userMessage) {
        messageList.add(userMessage);
        int pos = messageList.size() -1;
        adapter.notifyItemInserted(pos);
        chatBox.getLayoutManager().scrollToPosition(pos);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == send.getId()) {
            dataPasser.clicked(message.getText().toString());
            message.setText("");
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            dataPasser.clicked(message.getText().toString());
            message.setText("");
            return true;
        }
        return false;
    }
}

