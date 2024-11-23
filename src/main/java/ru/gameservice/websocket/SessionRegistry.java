package ru.gameservice.websocket;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {

    // Основная карта: ключ — gameId, значение — карта (playerId -> sessionId)
    private static final Map<UUID, Map<UUID, String>> gameSessions = new ConcurrentHashMap<>();

    // Карта для хранения сессии игрового поля (playerId == null)
    private static final Map<UUID, String> gameBoardSessions = new ConcurrentHashMap<>();

    // Добавление сессии игрока
    public static void addPlayerSession(UUID gameId, UUID playerId, String sessionId) {
        gameSessions.computeIfAbsent(gameId, k -> new ConcurrentHashMap<>()).put(playerId, sessionId);
    }

    // Добавление сессии игрового поля
    public static void addGameBoardSession(UUID gameId, String sessionId) {
        gameBoardSessions.put(gameId, sessionId);
    }

    // Получение sessionId игрока по gameId и playerId
    public static String getPlayerSessionId(UUID gameId, UUID playerId) {
        Map<UUID, String> players = gameSessions.get(gameId);
        if (players != null) {
            return players.get(playerId);
        }
        return null;
    }

    // Получение всех сессий игроков по gameId
    public static Map<UUID, String> getPlayerSessions(UUID gameId) {
        return gameSessions.get(gameId);
    }

    // Получение sessionId игрового поля по gameId
    public static String getGameBoardSessionId(UUID gameId) {
        return gameBoardSessions.get(gameId);
    }

    // Удаление сессии игрока по sessionId
    public static void removeSessionBySessionId(String sessionId) {
        // Удаляем из gameBoardSessions
        gameBoardSessions.entrySet().removeIf(entry -> entry.getValue().equals(sessionId));

        // Удаляем из gameSessions
        gameSessions.forEach((gameId, players) -> {
            players.entrySet().removeIf(entry -> entry.getValue().equals(sessionId));
            if (players.isEmpty()) {
                gameSessions.remove(gameId);
            }
        });
    }
}
