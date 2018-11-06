package me.lgbt.pubhub.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import me.lgbt.pubhub.R;

public class ChatFragment extends Fragment implements View.OnClickListener {

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
        message = act.findViewById(R.id.chatMessage);
        send = act.findViewById(R.id.sendMessage);
        chatBox = act.findViewById(R.id.chatBox);

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
        adapter.notifyItemInserted(messageList.size() - 1);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == send.getId()) {
            dataPasser.clicked(message.getText().toString());
            message.setText("");
        }
    }
}

