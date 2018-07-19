package com.rnd.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rnd.domain.Message;
import com.rnd.domain.Type;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {

    private Map<String, WebSocketSession> usersOnline = new HashMap<>();

    private ObjectMapper mapper = new ObjectMapper();

    public void userLogin(String login, WebSocketSession session) throws IOException {
        sendUsersOnline(session);

        Message message = new Message();
        message.setType(Type.USER_LOGIN);
        message.setUserLogin(login);
        TextMessage textMessage = new TextMessage(mapper.writeValueAsString(message));
        for (WebSocketSession webSocketSession: usersOnline.values()) {
            webSocketSession.sendMessage(textMessage);
        }

        usersOnline.put(login,session);
    }

    public void userLogout(String logout) throws IOException {
        usersOnline.remove(logout);

        Message message = new Message();
        message.setType(Type.USER_LOGOUT);
        message.setUserLogin(logout);
        TextMessage textMessage = new TextMessage(mapper.writeValueAsString(message));
        for (WebSocketSession webSocketSession: usersOnline.values()) {
            webSocketSession.sendMessage(textMessage);
        }
    }

    public WebSocketSession getUserSessionByLogin(String login) {
        return usersOnline.get(login);
    }

    public Set<String> usersOnline() {
        return usersOnline.keySet();
    }

    public void sendUsersOnline(WebSocketSession session) throws IOException {
        Message message = new Message();
        message.setType(Type.USERS_ONLINE);
        message.setContent(mapper.writeValueAsString(usersOnline.keySet()));

        session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
    }

}
