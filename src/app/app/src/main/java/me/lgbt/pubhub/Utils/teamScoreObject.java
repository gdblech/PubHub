package me.lgbt.pubhub.Utils;

/**
 * @author Geoffrey Blech
 *
 */
public class teamScoreObject {
    private int score;
    private String teamName;

    public teamScoreObject(String teamName, int score) {
        this.score = score;
        this.teamName = teamName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
