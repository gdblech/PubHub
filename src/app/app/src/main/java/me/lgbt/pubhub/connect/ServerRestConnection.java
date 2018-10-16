package me.lgbt.pubhub.connect;

/**
 * @author Geoffrey Blech
 *  Set of methods for interacting with PubHub REST API, all methods need to be called off of the main thread,
 *  It is suggested to do this with an AsyncTask.execute call.
 *
 * @since 10/1/2018
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64OutputStream;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import me.lgbt.pubhub.trivia.utils.TriviaGame;

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
     * Sends a picture to the server
     * @param url The url of the server
     * @param pictureUri  The Uri of the picture being uploaded
     * @param phbToken The PubHub token
     */
    public static String pushPicture(String url, Uri pictureUri, String phbToken) {

        Bitmap picture = BitmapFactory.decodeFile(pictureUri.getPath());
        String id = "";
        try {
            //Set up http connection
            String url1 = url + "/api/trivia/picture"; //TODO add correct REST location
            URL server = new URL(url1);
            HttpURLConnection backend = (HttpURLConnection) server.openConnection();
            backend.setRequestProperty("Authorization", "Bearer " + phbToken);
            backend.setRequestMethod("PUT");

            //convert picture into base64 for sending
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Base64OutputStream base = new Base64OutputStream(stream, 0);
            picture.compress(Bitmap.CompressFormat.PNG, 50, base);
            byte[] picBytes = stream.toByteArray();
            stream.close();


            backend.setDoOutput(true);
            backend.getOutputStream().write(picBytes);

            backend.connect();
            //get response from stream
            if (backend.getResponseCode() != 200) {
                throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
            }
            BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
            id = response.readLine();
            response.close();
            backend.disconnect();

        } catch (IOException e) {
            Log.w(TAG, " pushPicture: error ", e);
        }
        return id;
    }

    /**
     *  Pushes a trivia game JSON to the server
     * @param url The url of the server
     * @param gameJson The json of the game being sent to the server
     * @param phbToken The PubHub token
     */
    public static void pushTriviaGame(String url, String gameJson, String phbToken){
        String url1 = url + "/api/trivia";

        try {
            //REST API https setup
            URL server = new URL(url1);
            HttpURLConnection backend = (HttpURLConnection) server.openConnection();
            backend.setRequestProperty("Authorization", "Bearer " + phbToken);
            backend.setRequestMethod("GET");

            backend.setDoOutput(true);
            backend.getOutputStream().write(gameJson.getBytes());
            backend.connect();

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


