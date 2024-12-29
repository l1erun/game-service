package ru.gameservice.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.UUID;

@Component
public class WebSocketEventListener {

//    @EventListener
//    public void handleSessionDisconnected(SessionDisconnectEvent event) {
//        String sessionId = event.getSessionId();
//
//        // Удаляем сессии из реестра
//        SessionRegistry.removeSessionBySessionId(sessionId);
//        System.out.println("Session ID " + sessionId + " отключен");
//    }

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        System.out.println("Пользователь подключен. Логируем сессии:");
        logActiveSessions();
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        System.out.println("Пользователь отключен. Логируем сессии:");
        logActiveSessions();
    }

    private void logActiveSessions() {
        for (SimpUser user : simpUserRegistry.getUsers()) {
            System.out.println("Пользователь: " + user.getName());
            for (SimpSession session : user.getSessions()) {
                System.out.println("Сессия: " + session.getId());
            }
        }
    }
}
