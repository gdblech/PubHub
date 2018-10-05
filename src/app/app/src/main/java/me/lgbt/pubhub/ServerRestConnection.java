package me.lgbt.pubhub;

import android.graphics.Bitmap;
import android.util.Base64OutputStream;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRestConnection {

    static final String TAG = "ServerRESTConnection";

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

    public static String pushPicture(String url, Bitmap picture, String phbToken) {

        try {
            //Set up http connection
            String url1 = url + "/api/"; //TODO add correct REST location
            URL server = new URL(url1);
            HttpURLConnection backend = (HttpURLConnection) server.openConnection();
            backend.setRequestProperty("Authorization", "Bearer " + phbToken);
            backend.setRequestMethod("PUT");

            //convert picture into byte array for sending
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Base64OutputStream base = new Base64OutputStream(stream, 0);
            picture.compress(Bitmap.CompressFormat.PNG, 50, base);
            byte[] picBytes = stream.toByteArray();


            backend.setDoOutput(true);
            backend.getOutputStream().write(picBytes);

            backend.connect();
            //get response from stream
            if (backend.getResponseCode() != 200) {
                throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
            }
            backend.disconnect();

        } catch (IOException e) {
            Log.w(TAG, " pushPicture: error ", e);
        }

        return null;
    }

}
