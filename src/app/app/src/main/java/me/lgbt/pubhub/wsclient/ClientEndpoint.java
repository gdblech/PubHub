package me.lgbt.pubhub.wsclient;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import me.lgbt.pubhub.R;

/**
 * When messages are sent to the endpoints, they are automatically converted between
 * JSON and Java Objects by using the server endpoint annotation.
 **/

@ServerEndpoint(
        value = "/chat/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)
public class ClientEndpoint extends AppCompatActivity {

    private Session session;
    private static Set<ClientEndpoint> clientEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_chat_tab);
    }

    /*
     * When a new user logs in @OnOpen is immediately mapped to a data structure of active users
     */

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) {

        this.session = session;
        clientEndpoints.add(this);
        users.put(session.getId(), username);

//        Message message = new Message(); // message created and sent to all endpoints using the broadcast method
//        message.setFrom(username);
//        message.setContent("Connected!");
//        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message, JsonReader json)
            throws IOException, EncodeException {

        message.setFrom(users.get(session.getId()));
        broadcast(message);
    }

    /* Clears the endpoint and broadcasts to all users that a user has been disconnected */

    @OnClose
    public void onClose(Session session) {

        clientEndpoints.remove(this);
//        Message message = new Message();
//        message.setFrom(users.get(session.getId()));
//        message.setContent("Disconnected!");
//        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // TODO Do error handling here
    }

    /* Message sent to all endpoints */
    private static void broadcast(Message message) {

        for (ClientEndpoint ce : clientEndpoints)
            synchronized (ce) {
                try {
                    ce.session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }

    }
}