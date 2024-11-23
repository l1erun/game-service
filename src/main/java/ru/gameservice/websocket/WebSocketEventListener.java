package ru.gameservice.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.UUID;

@Component
public class WebSocketEventListener {

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();

        // Удаляем сессии из реестра
        SessionRegistry.removeSessionBySessionId(sessionId);
        System.out.println("Session ID " + sessionId + " отключен");
    }
}
