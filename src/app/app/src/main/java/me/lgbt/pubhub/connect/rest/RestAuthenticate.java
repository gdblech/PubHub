package me.lgbt.pubhub.connect.rest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Geoffrey Blech
 * @version 2.0
 * @since 10/22/2018
 *
 * Sends a google Token and gets back a PubHub token, extends Thread, use Thread commands to run.
 */
public class RestAuthenticate extends Thread {
    private final String TAG = "Authentication";
    private String url;
    private int mode = ConnectionTypes.HTTP;
    private String googleToken;
    private String phbToken;

    public RestAuthenticate(String url, String googleToken) {
        this.url = url + "/api/auth";
        this.googleToken = googleToken;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getPhbToken() {
        return phbToken;
    }

    public void run() {
        try {
            switch (mode) {
                case ConnectionTypes.HTTP: {
                    //Connect
                    URL server = new URL("http://" + url);
                    HttpURLConnection backend = (HttpURLConnection) server.openConnection();
                    connection(backend);
                    break;
                }

                case ConnectionTypes.HTTPS: {
                    //connection setup
                    URL server = new URL("https://" + url);
                    HttpsURLConnection backend = (HttpsURLConnection) server.openConnection();
                    connection(backend);
                    break;
                }
            }
        } catch (MalformedURLException e) {
            String message = "URL error: " + e.getMessage() + " \n Check value.xml for proper url";
            Log.e("Rest Authenticate Run", message);
        } catch (IOException e) {
            String message = "URL error: " + e.getMessage();
            Log.e("Rest Authenticate Run", message);
        }
    }

    private void connection(HttpsURLConnection backend) {
        try {
            //send out
            backend.setRequestProperty("Authorization", "Bearer " + googleToken);
            backend.setRequestMethod("GET");
            backend.connect();

            //get return
            if (backend.getResponseCode() == 200) {
                BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                phbToken = response.readLine();
                response.close();
            } else {
                throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
            }
            backend.disconnect();

        } catch (ProtocolException e) {
            //Request method is hard coded and should not throw an error. but if it does:
            String message = "ttp Url Connection error: " + e.getMessage() + "\n Check request method";
            Log.e("RestAuthenticate Conn", message);
        } catch (IOException e) {
            String message = "Http Url Connection error: " + e.getMessage();
            Log.e("RestAuthenticate Conn", message);
        }
    }

    private void connection(HttpURLConnection backend) {
        try {
            //send out
            backend.setRequestProperty("Authorization", "Bearer " + googleToken);
            backend.setRequestMethod("GET");
            backend.connect();

            //get return
            if (backend.getResponseCode() == 200) {
                BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                phbToken = response.readLine();
                response.close();
            } else {
                throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
            }
            backend.disconnect();

        } catch (ProtocolException e) {
            //Request method is hard coded and should not throw an error. but if it does:
            String message = "ttp Url Connection error: " + e.getMessage() + "\n Check request method";
            Log.e("RestAuthenticate Conn", message);
        } catch (IOException e) {
            String message = "Http Url Connection error: " + e.getMessage();
            Log.e("RestAuthenticate Conn", message);
        }
    }
}
