package com.example.blairgentry.pubhub_java.Trivia;

import java.util.HashMap;

public class TriviaSlide {
    private String title;
    private String question;
    private HashMap answers;
    private int score;
    private int style;
    private int slideTime;
    private TriviaPicture picture;

    TriviaSlide(String title) {
        this.title = title;
    }

    TriviaSlide(String title, String question, int score,TriviaPicture picture){
        this.title = title;
        this.question = question;
        this.score = score;
        this.picture = picture;
    }

    //future release
    public int getSlideTime() {
        return slideTime;
    }

    //future release
    public void setSlideTime(int slideTime) {
        this.slideTime = slideTime;
    }

    //future release
    public int getStyle() {
        return style;
    }

    //future release
    public void setStyle(int style) {
        this.style = style;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
    public boolean checkAnswer(String answer) {
        return false;
    }
}
