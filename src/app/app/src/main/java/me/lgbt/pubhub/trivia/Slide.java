package me.lgbt.pubhub.trivia;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Slide implements Parcelable {
    private String title;
    private String text;
    private Uri picture;

    Slide(){
        title = null;
        text = null;
        picture = null;
    }

    Slide(String title, String text, Uri picture){
        this.title = title;
        this.text = text;
        this.picture = picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeString(title);
        out.writeString(text);
        out.writeTypedObject(picture, flags);
        }

    public static final Parcelable.Creator<Slide> CREATOR = new Parcelable.Creator<Slide>() {
        public Slide createFromParcel(Parcel in) {
            return new Slide(in);
        }

        public Slide[] newArray(int size){
            return new Slide[size];
        }
    };

    Slide(Parcel in){
        title = in.readString();
        text = in.readString();
        picture = in.readTypedObject(Uri.CREATOR);
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

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public Uri getPicture() {
        return picture;
    }

}
