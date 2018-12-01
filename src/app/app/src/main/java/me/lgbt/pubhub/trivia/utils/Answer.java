package me.lgbt.pubhub.trivia.utils;

/**
 * @author Geoffrey Blech
 * The class representing an answer for passsing around between activities and fragments
 */
public class Answer {
    String answer;
    int teamID;
    boolean correct = false;

    public Answer(String answer, int teamID) {
        this.answer = answer;
        this.teamID = teamID;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
