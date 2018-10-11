package me.lgbt.pubhub.Trivia;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TriviaRound extends Slide implements Parcelable {
    private ArrayList<TriviaQuestion> questions;
    private boolean creationMode = false;

    TriviaRound(String title, String text, String picture){
        super(title, text, picture);
        questions = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        super.writeToParcel(out, flags);
        out.writeTypedList(questions);
        out.writeInt(creationMode ? 1 : 0);
    }

    public static final Parcelable.Creator<TriviaRound> CREATOR = new Parcelable.Creator<TriviaRound>() {
        public TriviaRound createFromParcel(Parcel in) {
            return new TriviaRound(in);
        }

        public TriviaRound[] newArray(int size){
            return new TriviaRound[size];
        }
    };

    private TriviaRound(Parcel in){
        super(in);
        questions = in.createTypedArrayList(TriviaQuestion.CREATOR);
        
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
