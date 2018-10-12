package me.lgbt.pubhub.Chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.lgbt.pubhub.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    class Message {
        String message;
        User sender;
        long createdAt;

        private String getMessage() {
            return this.message;
        }

        private void setMessage(String message) {
            this.message = message;
        }

        private User getSender() {
            return sender;
        }

        private void setSender(User sender) {
            this.sender = sender;
        }

        private long getCreatedAt() {
            return createdAt;
        }

        private void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }
    }

    class User {
        String nickname;

        private String getNickname() {
            return nickname;
        }

        private void setNickname(String nickname) {
            this.nickname = nickname;
        }

    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
            nameText.setText(message.getSender().getNickname());

            // Insert the profile image from the URL into the ImageView.
            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }
}


