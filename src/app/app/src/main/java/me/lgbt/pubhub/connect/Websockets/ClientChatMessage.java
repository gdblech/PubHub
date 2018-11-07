package me.lgbt.pubhub.connect.Websockets;

import javax.ws.rs.client.Client;

public class ClientChatMessage {

    private String message;

    public ClientChatMessage(String _message) {
        message = _message;
    }

    public String toString(){
      //  {"messageType":"ClientServerChatMessage","payload": {"message":"Chat works"}}
        String json = "{\"messageType\":\"ClientServerChatMessage\",\"payload\":{\"message\":\"" +
                message + "\"}}";
        return json;
    }
}
