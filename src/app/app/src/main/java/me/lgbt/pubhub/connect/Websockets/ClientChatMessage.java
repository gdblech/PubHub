package me.lgbt.pubhub.connect.Websockets;

import android.support.annotation.NonNull;

public class ClientChatMessage {

    private String message;

    public ClientChatMessage(String _message) {
        message = _message;
    }

    @NonNull
    public String toString(){
      //  {"messageType":"ClientServerChatMessage","payload": {"message":"Chat works"}}
        return "{\"messageType\":\"ClientServerChatMessage\",\"payload\":{\"message\":\"" +
                message + "\"}}";
    }
}