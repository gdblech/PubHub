package me.lgbt.pubhub.wsclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.lgbt.pubhub.R;


public class ChatTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_tab);
    }

    /* Displays a message received */

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText; // add name and prof pic

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

        }
    }
}
