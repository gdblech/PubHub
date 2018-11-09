package me.lgbt.pubhub.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.lgbt.pubhub.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private ArrayList<UserMessage> messageList;

    public MessageAdapter(ArrayList<UserMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View messageView = inflater.inflate(R.layout.message_object, parent, false);
        return new MessageAdapter.ViewHolder(messageView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int position) {
        UserMessage message = messageList.get(position);

        TextView time = viewHolder.time;
        TextView sender = viewHolder.sender;
        TextView text = viewHolder.text;

        text.setText(message.getText());
        sender.setText(message.getSender());
        time.setText(message.getTimeString());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        TextView sender;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.messageTime);
            text = itemView.findViewById(R.id.messageText);
            sender = itemView.findViewById(R.id.mesasgeSender);
        }
    }
}
