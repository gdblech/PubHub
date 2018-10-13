package me.lgbt.pubhub.Chat;

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

/*
 * When messages are sent to the endpoints,
 * they are automatically converted between
 * JSON and Java Objects by using the server
 * endpoint annotation.
 * */
@ServerEndpoint(
        value="/chat/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class )
public class ChatEndpoint {

    private Session session;
    private static Set<ChatEndpoint> chatEndpoints
            = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    /*
     * When a new user logs in @OnOpen is immediately mapped to a data structure of active users
     */

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) throws IOException, EncodeException {

        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), username);

        Message message = new Message(); // message created and sent to all endpoints using the broadcast method
        message.setFrom(username);
        message.setContent("Connected!");
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message)
            throws IOException, EncodeException {

        message.setFrom(users.get(session.getId()));
        broadcast(message);
    }

    /* Clears the endpoint and broadcasts to all users that a user has been disconnected */

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {

        chatEndpoints.remove(this);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("Disconnected!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // TODO Do error handling here
    }

    /* Message sent to all endpoints */
    private static void broadcast(Message message)
            throws IOException, EncodeException {

        for (ChatEndpoint ce : chatEndpoints)
            synchronized (ce) {
                try {
                    ce.session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        // For API level 24+ // TODO Make an if
        //        chatEndpoints.forEach(endpoint -> {
        //            synchronized (endpoint) {
        //                try {
        //                    endpoint.session.getBasicRemote().
        //                            sendObject(message);
        //                } catch (IOException | EncodeException e) {
        //                    e.printStackTrace();
        //                }
        //            }
    }
}