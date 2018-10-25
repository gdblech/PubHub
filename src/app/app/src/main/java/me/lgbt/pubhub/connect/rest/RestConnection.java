package me.lgbt.pubhub.connect.rest;

public class RestConnection extends Thread {


//todo implement this

    private String url;
    private int mode = ConnectionTypes.HTTP;
    private String phbToken;

    public RestConnection(String url, String phbToken) {
        this.url = url + "/api/trivia";
        this.phbToken = phbToken;
    }
}
