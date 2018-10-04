package com.example.blairgentry.pubhub_java;

import android.graphics.Bitmap;
import java.util.Base64;
import android.util.Log;

import com.example.blairgentry.pubhub_java.Trivia.TriviaPicture;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRestConnection {

    static final String TAG = "ServerRESTConnection";

    public static String authentication(String url, String googleToken) {
        String url1 = url + "/auth/api";
        String phbToken = null;

        try {
            //REST API https setup
            URL server = new URL("http://pubhub.me/api/auth");
            HttpURLConnection backend = (HttpURLConnection) server.openConnection();
            backend.setRequestProperty("Authorization", "Bearer " + googleToken);
            backend.setRequestMethod("GET");
            backend.connect();

            //ger response
            if (backend.getResponseCode() == 200) {
                BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                phbToken = response.readLine();
            } else {
                throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
            }
            backend.disconnect();

        } catch (IOException e) {
            Log.w(TAG, "Authentication: error", e);
        }

        return phbToken;
    }

    public static String pushPicture(String url, TriviaPicture picture, String phbToken) {

        try {
            //Set up http connection
            String url1 = url + "/api/"; //TODO add correct REST location
            URL server = new URL(url1);
            HttpURLConnection backend = (HttpURLConnection) server.openConnection();
            backend.setRequestProperty("Authorization", "Bearer " + phbToken);
            backend.setRequestMethod("PUT");

            //convert picture into byte array for sending
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.loadPicture().compress(Bitmap.CompressFormat.PNG,50, stream);

            byte[] picBytes = stream.toByteArray();

            //byte[] base64 = Base64.getEncoder();

            backend.setDoOutput(true);
            backend.getOutputStream().write(picBytes); // TODO need to write byte array from picture
            //TODO add code to turn into bit stream
            //TODO add code to send out picture may require buffer due to size

            backend.connect();
            //get response from stream
            if (backend.getResponseCode() == 200) {
                BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                picture.setLocationRemote(response.readLine());
            } else {
                throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
            }

            backend.disconnect();

        } catch (IOException e) {
            Log.w(TAG, " pushPicture: error ", e);
        }

        return null;
    }

}
