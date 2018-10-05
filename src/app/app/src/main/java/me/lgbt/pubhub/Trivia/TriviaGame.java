package me.lgbt.pubhub.Trivia;

import android.graphics.Bitmap;

public class TriviaGame extends Slide {
    private int ID;
    private String host; //TODO change the user object
    private long date;

    TriviaGame(String title, String text, Bitmap picture, int ID, String host, long date) {
        super(title, text, picture);
        this.ID = ID;
        this.host = host;
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
