package me.lgbt.pubhub.Trivia;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class TriviaRound extends Slide{
    private ArrayList<TriviaQuestion> questions;
    private boolean creationMode = false;

    TriviaRound(String title, String text, Bitmap picture){
        super(title,text,picture);
        questions = null;
    }

    public ArrayList<TriviaQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<TriviaQuestion> questions) {
        this.questions = questions;
    }

    public boolean addQuestion(TriviaQuestion question){
        if(questions != null){
            questions.add(question);
            return true;
        }else{
            return false;
        }
    }

    public boolean removeQuestion(int questionNumber){
        if(questions != null){
            questions.remove(questionNumber);
            return true;
        }else{
            return false;
        }

    }

    //initializes questions of value is true and creation mode is false, nulls questions if false and true.
    public void setCreationMode(boolean value){
        if(value && !creationMode){
            creationMode = true;
            questions = new ArrayList<>();
        }else if(!value && creationMode){
            creationMode = false;
            questions = null;
        }
    }

    public boolean getCreantionMode(){
        return creationMode;
    }

}
