package me.lgbt.pubhub.trivia.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
        convertPicture(pictureString);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    /* Takes in a string of base64 and outputs an image. */
    private void convertPicture(String pictureString) {
        byte[] decodedImage = Base64.decode(pictureString, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
        image = decodedBitmap;
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
