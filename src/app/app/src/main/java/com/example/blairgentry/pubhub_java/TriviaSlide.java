package com.example.blairgentry.pubhub_java;

import java.util.HashMap;

class TriviaSlide {
    private String title;
    private String question;
    private HashMap answers;
    private int value;
    private int style;
    private int slideTime;
    private TriviaPicture picture;

    TriviaSlide(String t) {
        title = t;
    }

    public int getSlideTime() {
        return slideTime;
    }

    //future release
    public void setSlideTime(int slideTime) {
        this.slideTime = slideTime;
    }

    public int getStyle() {
        return style;
    }

    //future release
    public void setStyle(int style) {
        this.style = style;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TriviaPicture getPicture() {
        return picture;
    }

    public void setPicture(TriviaPicture picture) {
        this.picture = picture;
    }

    //future release
    public boolean addAnswer(String answer) {
        return false;
    }

    //future release
    public boolean checkAnswer(String guess) {
        return false;
    }
}
