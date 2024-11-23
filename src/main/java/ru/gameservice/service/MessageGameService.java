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
    public void sendMessageToGame(UUID gameId, GameState gameState) {
        // Отправляем всем игрокам
        Map<UUID, String> playerSessions = SessionRegistry.getPlayerSessions(gameId);
        if (playerSessions != null) {
            for (String sessionId : playerSessions.values()) {
                String destination = "/queue/game/" + gameId;
                messagingTemplate.convertAndSendToUser(sessionId, destination, gameState);
            }
        }

        // Отправляем игровому полю
        String gameBoardSessionId = SessionRegistry.getGameBoardSessionId(gameId);
        if (gameBoardSessionId != null) {
            String destination = "/queue/game/" + gameId;
            messagingTemplate.convertAndSendToUser(gameBoardSessionId, destination, gameState);
        }
    }

    // Метод для отправки персонального сообщения конкретному игроку
    public void sendMessageToPlayer(UUID gameId, UUID playerId, Object message) {
        String sessionId = SessionRegistry.getPlayerSessionId(gameId, playerId);
        if (sessionId != null) {
            String destination = "/queue/game/" + gameId;
            System.out.println("Отправка сообщения игроку с ID " + playerId + " на путь " + destination + ": " + message);
            messagingTemplate.convertAndSendToUser(sessionId, destination, message);
        } else {
            System.out.println("Игрок с ID " + playerId + " не в сети или sessionId не найден");
        }
    }
}
