package me.lgbt.pubhub.connect.REST;

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
 * Set of methods for interacting with PubHub REST API,
 * @version 2.0
 * @since 10/22/2018
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
            //todo handle properly
        } catch (IOException e) {
            //todo handle properly
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
            //todo handle properly
        } catch (IOException e) {
            //todo handle properly
        }
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
            //todo fix for proper handling
        } catch (IOException e) {
            //todo handle properly
        }


    }
}
