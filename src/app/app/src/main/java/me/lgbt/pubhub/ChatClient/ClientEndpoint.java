package me.lgbt.pubhub.ChatClient;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class ClientEndpoint {

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {}

    /*
    ** Message Handler to forward all incoming text messages to all connected peers.
    ** Can also have an @OnMessage tag for binary and pong message types.
    **/
    @OnMessage
    public void onMessage(Session session, String msg) {
        try {
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen())
                    s.getBasicRemote().sendText(msg);
            }
        } catch(IOException e){}
    }

    @OnError
    public void onError(Session session, Throwable error) {}

    @OnClose
    public void onClose(Session session, CloseReason reason) {}




}
