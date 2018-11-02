package me.lgbt.pubhub.chat;

import android.util.JsonReader;
import android.util.JsonWriter;

import javax.ws.rs.client.Client;

import me.lgbt.pubhub.users.User;

public class Message {

    private Client sender;


    public class UserMessage {

        private int createTime;

        public int getCreateTime() {
            return this.createTime;
        }

        public User getSender() {
            return null;
        }

        public int getMessage() {
            return 0;
        }

    }

}
