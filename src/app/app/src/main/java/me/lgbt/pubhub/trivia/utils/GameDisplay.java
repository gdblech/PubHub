package me.lgbt.pubhub.trivia.utils;

/**
 * @author Geoffrey Blech
 * Class representing a game for display inside a recyclerView's list of games
 */
public class GameDisplay {
    private String name;
    private String host;
    private String date;
    private int id;

    public GameDisplay(String name, String host, String date, int id) {
        this.name = name;
        this.date = date;
        this.host = host;
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
