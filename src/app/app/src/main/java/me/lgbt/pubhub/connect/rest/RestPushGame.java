package me.lgbt.pubhub.connect.rest;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Geoffrey Blech
 * @version 2.0
 * @since 10/22/2018
 * <p>
 * Sends a a json of a trivia game to the backend, extends Thread, use Thread commands to run.
 */

public class RestPushGame extends Thread {
    private final String TAG = "Pushing Json";
    private String url;
    private int mode = ConnectionTypes.HTTP;
    private String phbToken;
    private String gameJSON;

    public RestPushGame(String url, String phbToken, String gameJSON) {
        this.url = url + "/api/trivia";
        this.phbToken = phbToken;
        this.gameJSON = gameJSON;
    }

    public void setMode(int mode) {
        this.mode = mode;
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
            backend.setRequestProperty("Authorization", "Bearer " + phbToken);
            backend.setRequestMethod("POST");
            backend.setChunkedStreamingMode(0);

            backend.setDoOutput(true);
            OutputStream out = new BufferedOutputStream(backend.getOutputStream());
            out.write(gameJSON.getBytes());

            //get response from server
            if (backend.getResponseCode() != 200) {
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
            backend.setRequestProperty("Authorization", "Bearer " + phbToken);
            backend.setRequestMethod("POST");
            backend.setRequestProperty("Content-Type", "application/json");
            // backend.setRequestProperty("Accept-Encoding", "identity");

            //           backend.setChunkedStreamingMode(0);

            backend.setDoOutput(true);
//            DataOutputStream wr = new DataOutputStream(backend.getOutputStream());
//            wr.writeBytes(gameJSON);
//            OutputStream out = new BufferedOutputStream(backend.getOutputStream());
            //           out.write(gameJSON.getBytes());
            backend.getOutputStream().write(gameJSON.getBytes());

            //get response from server
            if (backend.getResponseCode() != 200) {
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
        } finally {
            backend.disconnect();
        }
    }
}
