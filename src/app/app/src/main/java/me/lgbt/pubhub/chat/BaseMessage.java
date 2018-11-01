package me.lgbt.pubhub.chat;

import android.text.format.DateUtils;

import java.util.Date;

import me.lgbt.pubhub.users.User;

public class BaseMessage {

    public class UserMessage {

        private User sender;
        private User receiver;
        private Date date;


        public User getSender() {
            return null;
        }

        public int getMessage() {
            return 0;
        }

        public DateUtils getCreatedAt() {
            return null;
        }
    }

}
