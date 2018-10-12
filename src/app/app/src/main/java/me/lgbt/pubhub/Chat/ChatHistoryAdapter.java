package me.lgbt.pubhub.Chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class ChatHistoryAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<BaseMessage> mMessageList;

    public ChatHistoryAdapter(Context context, List<BaseMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    private RecyclerView.ViewHolder SentMessageHolder(){
// https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
    }

    private RecyclerView.ViewHolder ReceivedMessageHolder(){

    }
}
