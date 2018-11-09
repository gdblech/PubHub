package me.lgbt.pubhub.trivia.utils;

public class GameDisplay {
    private String name;
    private String host;
    private String date;
    private int id;

    public GameDisplay(String name, String host, String date, int id){
        this.name = name;
        this.date = date;
        this.host = host;
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
