package me.lgbt.pubhub.chat;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

@ApplicationScoped
class ChatHandler {
    private final Set<Session> sessions = new HashSet<>();

    public void addSession(Session s){
        sessions.add(s);
    }
    public void removeSession(Session s){
        sessions.remove(s);
    }
    public Set<Session> getSessions(){
        return this.sessions;
    }
}
