package com.rnd.domain;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String id;
    private List<WebSocketSession> sessions = new ArrayList<>();

    public Room(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<WebSocketSession> getSessions() {
        return sessions;
    }

    public boolean addSession(WebSocketSession session) {
        return sessions.add(session);
    }

    public boolean removeSession(WebSocketSession session) {
        return sessions.remove(session);
    }
}
