package me.lgbt.pubhub.connect;

/**
 * @author Geoffrey Blech
 * Set of methods for interacting with PubHub REST API, all methods need to be called off of the main thread,
 * It is suggested to do this with an AsyncTask.execute call.
 * @since 10/1/2018
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64OutputStream;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRestConnection {

    private static final String TAG = "ServerRESTConnection";

    /**
     * Authenticates with the backend
     * @param url The url of the server
     * @param googleToken Token received from google after login
     * @return PubHub JWT for user identification
     */
    public static String authentication(String url, String googleToken) {
        String phbToken = null;
        String url1 = url + "/api/auth";

        try {
            //REST API https setup
            URL server = new URL(url1);
            HttpURLConnection backend = (HttpURLConnection) server.openConnection();
            backend.setRequestProperty("Authorization", "Bearer " + googleToken);
            backend.setRequestMethod("GET");
            backend.connect();

            //get response from server
            if (backend.getResponseCode() == 200) {
                BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                phbToken = response.readLine();
                response.close();
            } else {
                throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
            }
            backend.disconnect();

        } catch (IOException e) {
            Log.w(TAG, "Authentication: error", e);
        }

        return phbToken;
    }

    /**
     *  Pushes a trivia game JSON to the server
     * @param url The url of the server
     * @param gameJson The json of the game being sent to the server
     * @param phbToken The PubHub token
     */
    public static void pushTriviaGame(String url, String gameJson, String phbToken) {
        String url1 = url + "/api/trivia";

        try {
            //REST API https setup
            URL server = new URL(url1);
            HttpURLConnection backend = (HttpURLConnection) server.openConnection();
            backend.setRequestProperty("Authorization", "Bearer " + phbToken);
            backend.setRequestMethod("GET");

            backend.setDoOutput(true);
            OutputStream out = new BufferedOutputStream(backend.getOutputStream());
            out.write(gameJson.getBytes());

            //get response from server
            if (backend.getResponseCode() != 200) {
                throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
            }
            backend.disconnect();

        } catch (IOException e) {
            Log.w(TAG, "Authentication: error", e);
        }
    }

}


