package ru.gameservice.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.gameservice.websocket.SessionRegistry;

import java.util.UUID;

@Service
public class MessageGameService {
    private final SimpMessagingTemplate messagingTemplate;
    public MessageGameService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Метод для отправки сообщения всем клиентам в игре
     * @param gameId
     * @param message
     */
    public void sendMessageToGame(UUID gameId, Object message) {
        try {
            messagingTemplate.convertAndSend("/queue/game/" + gameId, message);
        } catch (Exception e) {
            System.err.println("Ошибка отправки сообщения для игры " + gameId + ": " + e.getMessage());
        }
    }

    /**
     * Метод для отправки персонального сообщения конкретному игроку
     * @param gameId
     * @param playerId
     * @param message
     */
    public void sendMessageToPlayer(UUID gameId, UUID playerId, Object message) {
        String sessionId = SessionRegistry.getPlayerSessionId(gameId, playerId);
        if (sessionId != null) {
            String destination = "/queue/game/" + gameId + "/" + playerId;
            messagingTemplate.convertAndSend(destination, message);
        } else {
            System.err.println("Игрок с ID " + playerId + " не в сети или sessionId не найден");
        }
    }

    // Новый метод для отправки сообщения в топик
    public void sendMessageToTopic(UUID gameId, Object message) {
        String destination = "/topic/game/" + gameId;
        messagingTemplate.convertAndSend(destination, message);
    }
}
