// https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/HomeWebsocket/WebsocketHome.html#

package me.lgbt.pubhub.chat;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/*
 * RESOURCES
 *
 * API: JAVAX.WEBSOCKET
 *
 * Use wss (websocket secure)
 */

@ServerEndpoint("ws://pubhub.me:8082")
public class WebsocketHandler {

    @OnOpen
    public void openSession(Session session) {

    }

    @OnClose
    public void closeSession(Session session) {

    }

    @OnError
    public void onError(Throwable error){

    }

    @OnMessage
    public void messageHandler(String message, Session session) {

    }

}
