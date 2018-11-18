package me.lgbt.pubhub.connect;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author Geoffrey Blech
 * Class for interacting with PubHub REST API,
 * @version 2.1
 * @since 10/26/2018
 */

public class RestConnection extends Thread {
    public final static int FETCHPROFILE = 0;
    public final static int AUTHENTICATE = 1;
    public final static int SENDGAME = 2;
    public final static int GETGAMES = 3;
    public final static int GETGAME = 4;

    private String url;
    private String token;
    private String response;
    private String body;
    private int mode;
    private String apiSpot;

    public RestConnection(String url, String token, int mode) {
        this.url = url;
        this.token = token;
        this.mode = mode;
    }

    public RestConnection(String url, String token) {
        this.url = url;
        this.token = token;
        this.mode = FETCHPROFILE;
    }

    public void setBody(String body){
        this.body = body;
    }

    private void setMode(int mode) {
        this.mode = mode;
    }


    public String getResponse() {
        return response;
    }

    public void start(int mode){
        setMode(mode);
        this.start();
    }

    public void run() {
        switch (mode){
            case AUTHENTICATE:{
                authenticate();
                break;
            }
            case GETGAMES:{
                getGameList();
                break;
            }
            case SENDGAME:{
                if(body == null){
                    response = "No Body Defined";
                }else {
                    sendGame();
                }
                break;
            }
            case GETGAME:{
                getGame();
                break;
            }
            default:{
                fetchProfile();
                break;
            }

        }
    }

    private void authenticate(){
        HttpURLConnection backend = connection("/api/auth");
        if(backend != null){
            backend.setRequestProperty("Authorization", "Bearer " + token);
            try {
                backend.setRequestMethod("GET");
                backend.connect();

                if (backend.getResponseCode() == 200) {
                    BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                    this.response = response.readLine();
                    response.close();
                } else {
                    throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
                }
                backend.disconnect();

            } catch (ProtocolException e) {
                //error wont happen
            } catch (IOException e) {
                String message = "Http Url Connection error: " + e.getMessage();
                Log.e("RestAuthenticate Conn", message);
            }
            backend.disconnect();
        }
    }

    private void sendGame(){
        HttpURLConnection backend = connection("/api/trivia");
        if(backend != null){
            backend.setRequestProperty("Authorization", "Bearer " + token);
            try {
                backend.setRequestMethod("POST");
                backend.setRequestProperty("Content-Type", "application/json");
                backend.setDoOutput(true);
                backend.getOutputStream().write(body.getBytes());

                if (backend.getResponseCode() == 200) {
                    BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                    this.response = response.readLine();
                    response.close();
                } else {
                    throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
                }
                backend.disconnect();

            } catch (ProtocolException e) {
                //error wont happen
            } catch (IOException e) {
                String message = "Http Url Connection error: " + e.getMessage();
                Log.e("RestAuthenticate Conn", message);
            }
            backend.disconnect();
        }
    }

    private void getGame(){
        HttpURLConnection backend = connection("/api/trivia");
        if(backend != null) {
            backend.setRequestProperty("Authorization", "Bearer " + token);
            try {
                backend.setRequestMethod("GET");
                backend.connect();
                if (backend.getResponseCode() == 200) {
                    BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                    this.response = response.readLine();
                    response.close();
                } else {
                    throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
                }
                backend.disconnect();

            } catch (ProtocolException e) {
                //error wont happen
            } catch (IOException e) {
                String message = "Http Url Connection error: " + e.getMessage();
                Log.e("RestAuthenticate Conn", message);
            }
            backend.disconnect();
        }
    }

    private void getGameList(){
        HttpURLConnection backend = connection("/api/trivia");
        if(backend != null){
            backend.setRequestProperty("Authorization", "Bearer " + token);
            try {
                backend.setRequestMethod("GET");
                backend.connect();
                if (backend.getResponseCode() == 200) {
                    BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                    this.response = response.readLine();
                    response.close();
                } else {
                    throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
                }
                backend.disconnect();

            } catch (ProtocolException e) {
                //error wont happen
            } catch (IOException e) {
                String message = "Http Url Connection error: " + e.getMessage();
                Log.e("RestAuthenticate Conn", message);
            }
            backend.disconnect();
        }
    }

    private void fetchProfile(){
        HttpURLConnection backend = connection("/api/auth/profile");
        if(backend != null){
            backend.setRequestProperty("Authorization", "Bearer " + token);
            try {
                backend.setRequestMethod("GET");
                backend.connect();
                if (backend.getResponseCode() == 200) {
                    BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                    this.response = response.readLine();
                    response.close();
                } else {
                    throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
                }
                backend.disconnect();

            } catch (ProtocolException e) {
                //error wont happen
            } catch (IOException e) {
                String message = "Http Url Connection error: " + e.getMessage();
                Log.e("RestAuthenticate Conn", message);
            }
        }
    }

    private HttpURLConnection connection(String endPoint){

        try {
            String prefix = "http://";
            URL server = new URL( prefix + url + endPoint);
            return (HttpURLConnection) server.openConnection();
        } catch (MalformedURLException e) {
            String message = "URL error: " + e.getMessage() + " \n Check value.xml for proper url";
            Log.e("Rest Authenticate Run", message);
        } catch (IOException e) {
            String message = "URL error: " + e.getMessage();
            Log.e("Rest Authenticate Run", message);
        }
        return null;
    }
}
