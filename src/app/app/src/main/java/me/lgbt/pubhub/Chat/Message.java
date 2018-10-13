/*
* Resources:
* https://www.baeldung.com/java-websockets
* https://guides.codepath.com/android/using-the-recyclerview
* https://www.sitepoint.com/real-time-apps-websockets-server-sent-events/
* */

package me.lgbt.pubhub.Chat;

public class Message {
    private String from;
    private String to;
    private String content;

    public Message() {
    }

    public Message(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}