package me.lgbt.pubhub.wsclient;

public class Message {

    int id;                 // chat identification number
    int userId;             // user id of the user who sent/is sending message
    String message;         // message content
    String timestamp;       // timestamp


    public String getMessage() {
        return this.message;
    }
}
