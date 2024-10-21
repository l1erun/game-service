package ru.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gameservice.dto.GameSessionRequest;
import ru.gameservice.entity.GameSession;
import ru.gameservice.entity.Player;
import ru.gameservice.repository.GameSessionRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для управления игровыми сессиями.
 */
@Service
public class ActionService {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private GameService gameService;

    /**
     * Создает новую игровую сессию.
     */
    public GameSession createGameSession() {
        GameSession session = new GameSession();
        session.setSessionId(UUID.randomUUID());
        session.setCreatedAt(System.currentTimeMillis());
        // Инициализация других полей
        gameSessionRepository.save(session);
        return session;
    }

    /**
     * Получает игровую сессию по идентификатору.
     */
    public Optional<GameSession> getGameSession(UUID sessionId) {
        return gameSessionRepository.findById(sessionId);
    }

    /**
     * Добавляет игрока в игровую сессию.
     */
    public GameSession addPlayerToSession(UUID sessionId, Player player) {
        GameSession session = getGameSession(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        session.getPlayers().add(player);
        gameSessionRepository.save(session);
        return session;
    }

    /**
     * Запускает игровую сессию.
     */
    public GameSession startGameSession(UUID sessionId, GameSessionRequest gameSessionRequest) {
        // Инициализация игрового состояния
        GameSession session = getGameSession(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        ruleService.initializeGame(session, gameSessionRequest);
//        gameSessionRepository.save(session);
        return session;
    }

    /**
     * Обрабатывает действие игрока.
     */
    public GameSession performAction(UUID sessionId, UUID playerId, String actionType, Object actionData) {
        GameSession session = getGameSession(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        gameService.processAction(session, playerId, actionType, actionData);
        gameSessionRepository.save(session);
        return session;
    }

    /**
     * Завершает ход игрока.
     */
    public GameSession endTurn(UUID sessionId, UUID playerId) {
        GameSession session = getGameSession(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        ruleService.endPlayerTurn(session, playerId);
        gameSessionRepository.save(session);
        return session;
    }

    // Другие необходимые методы
}
