package me.lgbt.pubhub.chat;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserMessage extends Message{

    private long time;
    private String text;
    private String sender;

    public UserMessage(String text, String sender, long time){
        this.sender = sender;
        this.text = text;
        this.time = time;
    }

    public UserMessage(String text, String sender, String timeStamp){
        this.sender = sender;
        this.text = text;
        this.time = convertTime(timeStamp);
    }

    public String getText() {
        return text;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return this.time;
    }

    public String getSender() {
        return sender;
    }

    public int getMessage() {
        return 0;
    }

    public String getTimeString(){
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.US);
        return sdf.format(time);
    }

    private long convertTime(String timeStamp){
//        timeStamp = timeStamp.replace("Z", "");
//        Timestamp stamp = Timestamp.valueOf(timeStamp);
//        return stamp.getTime() / 1000L;
        return 0;
    }

}
