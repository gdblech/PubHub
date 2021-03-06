package me.lgbt.pubhub.trivia.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import me.lgbt.pubhub.chat.Message;

/**
 * @author Geoffrey Blech
 */
public class TriviaMessage extends Message {
    private String title;
    private String text;
    private Bitmap image;
    private int currentRound;
    private int currentQuestion;
    private boolean isQuestion = false;

    public TriviaMessage(String title, String text, String pictureString, int currentRound, int currentQuestion) {
        this.title = title;
        this.text = text;
        convertPicture(pictureString);
        this.currentRound = currentRound;
        this.currentQuestion = currentQuestion;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
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

    public boolean isQuestion(boolean question) {
        isQuestion = question;
        return isQuestion;
    }
}
