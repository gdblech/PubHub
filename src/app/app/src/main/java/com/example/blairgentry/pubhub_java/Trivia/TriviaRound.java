package com.example.blairgentry.pubhub_java.Trivia;

import java.util.ArrayList;

public class TriviaRound {
    private ArrayList slides;
    private boolean example;
    private int numOfSlides;
    private int style;
    private boolean playBack;
    private int currentSlide;
    private int totalScore;

    TriviaRound(int numberOfSlides) {
        numOfSlides = numberOfSlides;
        slides = new ArrayList<TriviaSlide>(numberOfSlides);
        totalScore = 0;
        currentSlide = 0;
    }

    public boolean nextSlide() {
        if (currentSlide == numOfSlides) {
            return false;
        } else {
            currentSlide++;
            return true;
        }
    }

    public boolean prevSlide() {
        if (currentSlide == 0) {
            return false;
        } else {
            currentSlide--;
            return true;
        }
    }

    public boolean addSlide(TriviaSlide slide) {
        if (slides.size() == numOfSlides) {
            return false;
        } else {
            slides.add(slide);
            return true;
        }
    }

    public boolean addSlide(TriviaSlide slide, int place) {
        if (slides.size() == numOfSlides) {
            return false;
        } else {
            totalScore += slide.getScore();
            slides.add(place, slide);
            return true;
        }
    }

    public boolean removeSlide(int place) {
        if (slides.size() == 0) {
            return false;
        } else {
            slides.remove(place);
            return true;
        }
    }

    public int getNumOfSlides() {
        return numOfSlides;
    }

    public void setNumOfSlides(int numOfSlides) {
        this.numOfSlides = numOfSlides;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setSlides(ArrayList slides) {
        this.slides = slides;
    }

    public boolean getExample() {
        return example;
    }

    public void setExample(boolean example) {
        this.example = example;
    }

    //future release all following
    public void runRound() {
    }

    private void revRound() {
    }

    public void pauseRound() {
    }

    public void resumeRound() {
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public boolean getPlayBack() {
        return playBack;
    }

    public void setPlayBack(boolean playBack) {
        this.playBack = playBack;
    }
}

