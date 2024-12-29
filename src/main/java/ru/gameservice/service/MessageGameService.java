package ru.gameservice.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.gameservice.entity.GameState;
import ru.gameservice.websocket.SessionRegistry;

import java.util.Map;
import java.util.UUID;

@Service
public class MessageGameService {
    private final SimpMessagingTemplate messagingTemplate;
    public MessageGameService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Метод для отправки сообщения всем клиентам в игре
    public void sendMessageToGame(UUID gameId, Object message) {
        String destination = "/queue/game/" + gameId;
//        System.out.println("Отправка сообщения карте с ID " + destination + ": " + message);
        messagingTemplate.convertAndSend(destination, message);
    }

    // Метод для отправки персонального сообщения конкретному игроку
    public void sendMessageToPlayer(UUID gameId, UUID playerId, Object message) {
        String sessionId = SessionRegistry.getPlayerSessionId(gameId, playerId);
        if (sessionId != null) {
            String destination = "/queue/game/" + gameId + "/" + playerId;
//            System.out.println("Отправка сообщения игроку с ID " + playerId + " на путь " + destination + ": " + message);
            messagingTemplate.convertAndSend(destination, message);
        } else {
            System.out.println("Игрок с ID " + playerId + " не в сети или sessionId не найден");
        }
    }

    // Новый метод для отправки сообщения в топик
    public void sendMessageToTopic(UUID gameId, Object message) {
        String destination = "/topic/game/" + gameId;
//        System.out.println("Отправка сообщения в топик: " + destination + " сообщение: " + message);
        messagingTemplate.convertAndSend(destination, message);
    }
}
