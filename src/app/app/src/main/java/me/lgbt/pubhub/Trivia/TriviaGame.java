package me.lgbt.pubhub.Trivia;

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
    private int ID;
    private String host; //TODO change the user object
    private long date;
    private ArrayList<TriviaRound> rounds = null;
    private boolean creationMode = false;

    public TriviaGame(String title, String text, Uri picture, int ID, String host, long date) {
        super(title, text, picture);
        this.ID = ID;
        this.host = host;
        this.date = date;
        rounds = null;
    }

    public TriviaGame(){
        super();
        ID = 0;
        host = "";
        date = 0;
    }

    private TriviaGame(Parcel in) {
        super(in);
        ID = in.readInt();
        host = in.readString();
        date = in.readLong();
        rounds = in.createTypedArrayList(TriviaRound.CREATOR);
        creationMode =  in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(ID);
        out.writeString(host);
        out.writeLong(date);
        out.writeTypedList(rounds);
        out.writeInt(creationMode ? 1 : 0);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
            rounds.add(round);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeRound(int roundNumber) {
        if (rounds != null) {
            rounds.remove(roundNumber);
            return true;
        } else {
            return false;
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

    public int getNumberOfRounds(){
        return rounds.size();
    }
}
