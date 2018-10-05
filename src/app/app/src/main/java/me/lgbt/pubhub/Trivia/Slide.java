package me.lgbt.pubhub.Trivia;

import android.graphics.Bitmap;

public class Slide {
    private String title;
    private String text;
    private Bitmap picture;

    Slide(){
        title = null;
        text = null;
        picture = null;
    }


    Slide(String title, String text, Bitmap picture){
        this.title = title;
        this.text = text;
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String question) {
        this.text = question;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public Bitmap getPicture() {
        return picture;
    }

}
