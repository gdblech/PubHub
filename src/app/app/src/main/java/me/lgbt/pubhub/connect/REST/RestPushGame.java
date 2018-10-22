package me.lgbt.pubhub.connect.REST;

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
 * Set of methods for interacting with PubHub REST API,
 * @version 2.0
 * @since 10/22/2018
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

        private void connection(HttpsURLConnection backend) {
            try {
                backend.setRequestProperty("Authorization", "Bearer " + phbToken);
                backend.setRequestMethod("GET");

                backend.setDoOutput(true);
                OutputStream out = new BufferedOutputStream(backend.getOutputStream());
                out.write(gameJSON.getBytes());

                //get response from server
                if (backend.getResponseCode() != 200) {
                    throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
                }
                backend.disconnect();
            } catch (ProtocolException e) {
                //todo handle peropelry
            } catch (IOException e) {
                //todo handle properly
            }
        }

        private void connection(HttpURLConnection backend) {
            try {
                backend.setRequestProperty("Authorization", "Bearer " + phbToken);
                backend.setRequestMethod("GET");

                backend.setDoOutput(true);
                OutputStream out = new BufferedOutputStream(backend.getOutputStream());
                out.write(gameJSON.getBytes());

                //get response from server
                if (backend.getResponseCode() != 200) {
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
