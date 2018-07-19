package com.rnd.service;

import com.rnd.domain.Room;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class RoomService {

    private HashMap<String, Room> rooms = new HashMap<>();

    public String createRoom() {
        String id = UUID.randomUUID().toString();
        Room room = new Room(id);
        rooms.put(id, room);
        return id;
    }

    public boolean checkAccessToRoom(Principal mailSender, String roomId) {
        String loginMailSender = mailSender.getName();
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Invalid room id");
        }
        // Находится ли пользовать, который пригласил другого в этом же чате
        return room.getSessions().
                stream()
                .map(session1 -> session1.getPrincipal().getName())
                .anyMatch(o -> Objects.equals(loginMailSender, o));
    }

    public boolean connectToRoom(String roomId, WebSocketSession session) {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Invalid room id");
        }
        return room.addSession(session);
    }


    public void sendMessage(String roomId, TextMessage message) {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Invalid room id");
        }
        for (WebSocketSession session : room.getSessions()) {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void userLogout(WebSocketSession sessionOut) {
        Iterator<Map.Entry<String, Room>> it = rooms.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Room> entry = it.next();
            Room room = entry.getValue();
            room.getSessions().remove(sessionOut);
            if (room.getSessions().isEmpty()) {
                it.remove();
            }
        }
    }
}
