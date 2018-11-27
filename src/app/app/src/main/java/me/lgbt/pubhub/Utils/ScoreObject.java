package me.lgbt.pubhub.Utils;

import android.support.annotation.NonNull;

import java.util.Comparator;

public class ScoreObject {
    private int score;
    private String teamName;

    public ScoreObject(String teamName, int score){
        this.score = score;
        this.teamName = teamName;
    }

    public int getScore() {
        return score;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
