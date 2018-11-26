package me.lgbt.pubhub.trivia.utils;

public class Answer {
    String answer;
    int teamID;
    boolean correct= false;

    public Answer(String answer, int teamID){
        this.answer = answer;
        this.teamID = teamID;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public int getTeamID() {
        return teamID;
    }

    public String getAnswer() {
        return answer;
    }
}
