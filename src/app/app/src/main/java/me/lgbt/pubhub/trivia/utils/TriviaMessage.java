package me.lgbt.pubhub.trivia.utils;

import android.net.Uri;

import me.lgbt.pubhub.chat.Message;

public class TriviaMessage extends Message {
    private String title;
    private String text;
    private Uri picture;
    private boolean isQuestion = false;

    public TriviaMessage(String title, String text, Uri picture) {
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

    public Uri getPicture() {
        return picture;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isQuestion() {
        return isQuestion;
    }

    public void setQuestion(boolean question) {
        isQuestion = question;
    }
}
