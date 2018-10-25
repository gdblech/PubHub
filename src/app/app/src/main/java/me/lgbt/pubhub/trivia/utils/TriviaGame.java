package me.lgbt.pubhub.trivia.utils;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TriviaGame extends Slide implements Parcelable {
    public static final Parcelable.Creator<TriviaGame> CREATOR = new Parcelable.Creator<TriviaGame>() {
        public TriviaGame createFromParcel(Parcel in) {
            return new TriviaGame(in);
        }

        public TriviaGame[] newArray(int size) {
            return new TriviaGame[size];
        }
    };
    private String gameName;

    private ArrayList<TriviaRound> rounds = null;
    private boolean creationMode = false;

    public TriviaGame(String title, String text, Uri picture, String gameName) {
        super(title, text, picture);
        this.gameName = gameName;
    }

    public TriviaGame() {
        super();
        gameName = "";
    }

    private TriviaGame(Parcel in) {
        super(in);
        gameName = in.readString();
        rounds = in.createTypedArrayList(TriviaRound.CREATOR);
        creationMode = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(gameName);
        out.writeTypedList(rounds);
        out.writeInt(creationMode ? 1 : 0);
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ArrayList<TriviaRound> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<TriviaRound> rounds) {
        this.rounds = rounds;
        creationMode = true;
    }

    public boolean addRound(TriviaRound round) {
        if (rounds != null) {
            return rounds.add(round);
        } else {
            return false;
        }
    }

    public TriviaRound removeRound(int roundNumber) {
        if (rounds != null) {
            return rounds.remove(roundNumber);
        } else {
            return null;
        }

    }

    //initializes rounds of value is true and creation mode is false, nulls rounds if false and true.
    public void setCreationMode(boolean value) {
        if (value && !creationMode) {
            creationMode = true;
            rounds = new ArrayList<>();
        } else if (!value && creationMode) {
            creationMode = false;
            rounds = null;
        }
    }

    public boolean getCreantionMode() {
        return creationMode;
    }

    public int getTotalCount() {
        int count = 1;

        for (TriviaRound r : rounds) {
            count += r.getNumberOfQuestion() + 1;
        }

        return count;
    }

    public void trimQuestion() {
        if (rounds != null) {
            rounds.trimToSize();
        }
    }
}
