package me.lgbt.pubhub.Trivia;

import android.os.Parcel;
import android.os.Parcelable;
import android.net.Uri;

public class TriviaQuestion extends Slide implements Parcelable {
    private String answer;

    public TriviaQuestion(String title, String text, Uri picture, String answer){
        super(title, text, picture);
        this.answer = answer;
    }

    public TriviaQuestion(){
        super();
        answer = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        super.writeToParcel(out, flags);
        out.writeString(answer);
    }

    public static final Parcelable.Creator<TriviaQuestion> CREATOR = new Parcelable.Creator<TriviaQuestion>() {
        public TriviaQuestion createFromParcel(Parcel in) {
            return new TriviaQuestion(in);
        }

        public TriviaQuestion[] newArray(int size){
            return new TriviaQuestion[size];
        }
    };

    private TriviaQuestion(Parcel in){
        super(in);
        answer = in.readString();
    }

}
