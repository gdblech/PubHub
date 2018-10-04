package com.example.blairgentry.pubhub_java.Trivia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TriviaPicture {
    private String locationLocal;
    private String locationRemote;
    private File pictureFile;

    TriviaPicture(String local) {
        locationLocal = local;
        locationRemote = null;
        pictureFile = new File(local);
    }

    TriviaPicture() {
        locationLocal = null;
        locationRemote = null;
        pictureFile = null;
    }

    public String getLocationLocal() {
        return locationLocal;
    }

    public void setLocationLocal(String locationLocal) {
        this.locationLocal = locationLocal;
        pictureFile = new File(locationLocal);
    }

    public String getLocationRemote() {
        return locationRemote;
    }

    public void setLocationRemote(String locationRemote) {
        this.locationRemote = locationRemote;
    }

    public Bitmap loadPicture(){
        if(pictureFile.exists()){
            return BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
        }else{
            return null;
        }
    }

    //sends locally saved picture to the backend REST
    public boolean push(final String phbToken) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL server = new URL("http://pubhub.me/api/"); //TODO add correct REST location
                    HttpURLConnection backend = (HttpURLConnection) server.openConnection();
                    backend.setRequestProperty("Authorization", "Bearer " + phbToken);
                    backend.setRequestMethod("PUT");
                    //TODO add code to load image into memory
                    //TODO add code to turn int bit stream
                    //TODO add code to send out picture may require buffer due to size

                    backend.connect();
                    //get response from stream
                    if (backend.getResponseCode() == 200) {
                        BufferedReader response = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                        locationRemote = response.readLine();
                    } else {
                        throw new IOException("Http Code: " + backend.getResponseCode() + ", " + backend.getResponseMessage());
                    }

                    backend.disconnect();

                } catch (IOException e) {
                    Log.w("Picture Push", " GET: error", e);
                }
            }
        });

        return locationRemote != null;
    }

    //gets picture from the backend and saves it locally WS
    public boolean pull() {
        //TODO implement pic pull
        return false;
    }
}
