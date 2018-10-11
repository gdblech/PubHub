package me.lgbt.pubhub.Trivia;

import android.os.Parcel;
import android.os.Parcelable;

public class Slide implements Parcelable {
    private String title;
    private String text;
    private String picture;

    Slide(){
        title = null;
        text = null;
        picture = null;
    }

    Slide(String title, String text, String picture){
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
        out.writeString(picture);
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
        picture = in.readString();
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

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }

}
