package com.example.blairgentry.pubhub_java;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class ServerRestConnection {

    //get token
    static String authenticate(String googleToken, String TAG) {
        String phbToken = "";
        try {

            //REST API https setup
            String url = "pubhub.me/api/auth";
            URL server = new URL(url);
            HttpURLConnection backend = (HttpURLConnection) server.openConnection();
            backend.setRequestProperty("Authorization", "Bearer " + googleToken);
            backend.setRequestMethod("GET");

            backend.connect();

            //get response from stream
            if (backend.getResponseCode() == 200) {
                BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                phbToken = response.readLine();
            } else {
                throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
            }

            backend.disconnect();
        } catch (IOException e) {
            Log.w(TAG, "Authenticate: error", e);
        }

        return phbToken;
    }

    //get profile


    //put trivia

}


//AsyncTask.execute(new Runnable() {
//@Override
//public void run() {
//        String phbToken;
//        try {
//        //REST API https setup
//        String url = getString(R.string.server_backend) + "/api/auth";
//        URL server = new URL(url);
//        HttpURLConnection backend = (HttpURLConnection) server.openConnection();
//        backend.setRequestProperty("Authorization", "Bearer " + googleToken);
//        backend.setRequestMethod("GET");
//        backend.connect();
//
//        //ger response
//        if (backend.getResponseCode() == 200) {
//        BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
//        phbToken = response.readLine();
//        } else {
//        throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
//        }
//
//        backend.disconnect();
//
//
//
//        } catch (IOException e) {
//        Log.w(TAG, "Authenticate: error", e);
//        }
//        });
//        }
