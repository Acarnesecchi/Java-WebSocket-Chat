package aar.websockets.websocket;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;

import java.util.HashSet;
import java.util.Set;
import javax.json.JsonObject;


@ApplicationScoped
public class ChatSessionHandler {
    private int deviceId = 0;
    private final Set<Session> sessions = new HashSet<>();


    public void addSession(Session session) {
        sessions.add(session);
    }

    public void addChatIdProperty(Session session, int chatId) {
        if (session.getUserProperties().containsKey("chatId")) {
            session.getUserProperties().replace("chatId", chatId);
        } else {
            session.getUserProperties().put("chatId", chatId);
        }
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void sendToChatSession(int chatId, JsonObject message) {
        for (Session session : sessions) {
            if ((session.getUserProperties().containsKey("chatId")) &&
                    (chatId == (int) session.getUserProperties().get("chatId"))) {
                    sendToSession(session, message);
            }
        }
    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    public void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(ChatSessionHandler.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
    public void sendToSession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(ChatSessionHandler.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }


}