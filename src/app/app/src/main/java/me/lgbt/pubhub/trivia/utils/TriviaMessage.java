package me.lgbt.pubhub.trivia.utils;

import android.net.Uri;
import android.graphics.Bitmap;

import me.lgbt.pubhub.chat.Message;

public class TriviaMessage extends Message {
    private String title;
    private String text;
    private String pictureString;
    private Bitmap image;
    private boolean isQuestion = false;

    public TriviaMessage(String title, String text, String pictureString) {
        this.title = title;
        this.text = text;
        this.image = convertPicture(pictureString);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    /* Takes in a string of base64 and outputs an image. */
    private Bitmap convertPicture(String pictureString) {
        byte[] decodedImage = Base64.decode(pictureString, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isQuestion() {
        return isQuestion;
    }

    public void setQuestion(boolean question) {
        isQuestion = question;
    }
}
