package me.lgbt.pubhub.trivia.utils;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TriviaRound extends Slide implements Parcelable {
    public static final Parcelable.Creator<TriviaRound> CREATOR = new Parcelable.Creator<TriviaRound>() {
        public TriviaRound createFromParcel(Parcel in) {
            return new TriviaRound(in);
        }

        public TriviaRound[] newArray(int size) {
            return new TriviaRound[size];
        }
    };
    private ArrayList<TriviaQuestion> questions = null;
    private boolean creationMode = false;

    public TriviaRound(String title, String text, Uri picture) {
        super(title, text, picture);
    }

    public TriviaRound() {
        super();
    }

    private TriviaRound(Parcel in) {
        super(in);
        questions = in.createTypedArrayList(TriviaQuestion.CREATOR);
        creationMode = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeTypedList(questions);
        out.writeInt(creationMode ? 1 : 0);
    }

    public ArrayList<TriviaQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<TriviaQuestion> questions) {
        this.questions = questions;
    }

    public boolean addQuestion(TriviaQuestion question) {
        if (questions != null) {
            return questions.add(question);
        } else {
            return false;
        }
    }

    public TriviaQuestion removeQuestion(int questionNumber) {
        if (questions != null) {
            return questions.remove(questionNumber);
        } else {
            return null;
        }

    }

    //initializes questions of value is true and creation mode is false, nulls questions if false and true.
    public void setCreationMode(boolean value) {
        if (value && !creationMode) {
            creationMode = true;
            questions = new ArrayList<>();
        } else if (!value && creationMode) {
            creationMode = false;
            questions = null;
        }
    }

    public boolean getCreantionMode() {
        return creationMode;
    }

    public void trimQuestions() {
        if (questions != null) {
            questions.trimToSize();
        }
    }

    public int getNumberOfQuestion() {
        return questions.size();
    }
}
