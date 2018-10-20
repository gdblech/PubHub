package me.lgbt.pubhub.wsclient;

public class Message {

    int id;                 // chat identification number
    int userId;             // user id of sender
    String username;        // username of sender
    String message;         // message content
    String timestamp;       // timestamp
    String json;

    public Message(int id, int userId, String username, String message, String timestamp, String json) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.json = json;
    }

    public String getMessage() {
        return this.message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public void setFrom(String username) {
    }

    public void setContent(String s) {
    }
}
