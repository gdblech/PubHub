package com.example.blairgentry.pubhub_java.Trivia;

import java.util.ArrayList;

public class TriviaGame {
    private int numberOfRounds;
    private String gameName; //should never be changed
    private int gamePoints;
    private ArrayList<TriviaRound> rounds;
    private int currentRound;

    TriviaGame(String name, int numRounds) {
        gameName = name;
        numberOfRounds = numRounds;
        gamePoints = 0;
        currentRound = 0;
        rounds = new ArrayList<>(numberOfRounds);
    }

    public ArrayList getRounds() {
        return rounds;
    }

    public TriviaRound getCurrentRound() {
        return rounds.get(currentRound);
    }

    public int getCurrentRoundNumber(){
        return currentRound;
    }

    public int getGamePoints() {
        return gamePoints;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public String getGameName() {
        return gameName;
    }

    public boolean nextRound(){
        if(currentRound == numberOfRounds){
            return false;
        }else{
            currentRound++;
            return true;
        }
    }

    public boolean prevRound(){
        if(currentRound == 0){
            return false;
        }else{
            currentRound--;
            return true;
        }
    }

    public void setRounds(ArrayList<TriviaRound> rounds) {
        this.rounds = rounds;
    }

    public boolean addRound(TriviaRound round){
        if (rounds.size() == numberOfRounds){
            return  false;
        }else {
            rounds.add(round);
            return true;
        }
    }

    public boolean addRound(TriviaRound round, int place){
        if (rounds.size() == numberOfRounds){
            return  false;
        }else {
            rounds.add(place, round);
            gamePoints += round.getTotalScore();
            return true;
        }
    }
}
