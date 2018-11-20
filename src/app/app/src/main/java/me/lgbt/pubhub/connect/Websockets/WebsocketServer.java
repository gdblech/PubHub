// https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/HomeWebsocket/WebsocketHome.html#

package me.lgbt.pubhub.connect.Websockets;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import me.lgbt.pubhub.chat.UserMessage;
import okhttp3.WebSocket;

/*
 * RESOURCES
 *
 * API: JAVAX.WEBSOCKET
 *
 * Use wss (websocket secure)
 */
@ApplicationScoped
@ServerEndpoint("ws://pubhub.me:8082")
class WebsocketServer {

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
