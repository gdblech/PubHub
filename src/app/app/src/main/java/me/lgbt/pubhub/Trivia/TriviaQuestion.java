package me.lgbt.pubhub.Trivia;

import android.graphics.Bitmap;

public class TriviaQuestion extends Slide{
    private String answer;

    TriviaQuestion(String title, String text, Bitmap picture, String answer){
        super(title, text, picture);
        this.answer = answer;
    }


}
