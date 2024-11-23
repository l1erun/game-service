package ru.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gameservice.dto.GameSessionRequest;
import ru.gameservice.dto.PlayerDto;
import ru.gameservice.entity.GameSession;
import ru.gameservice.entity.Player;
import ru.gameservice.entity.Resources;
import ru.gameservice.repository.GameSessionRepository;
import ru.gameservice.repository.PlayerRepository;

import java.util.List;
import java.util.NoSuchElementException;
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

    @Autowired
    private MessageGameService messageGameService;

    @Autowired
    private PlayerRepository playerRepository;

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
    public GameSession addPlayerToSession(String pin, PlayerDto playerDto) {
        GameSession session = null;
        for (GameSession gameSession:gameSessionRepository.findAll()) {
            if (gameSession.getPin().equals(pin)){
                session = gameSession;
            }
        }
        if (session == null){
            throw new NoSuchElementException("Сессия с PIN-кодом " + pin + " не найдена");
        }
        Player player = new Player();
        player.setPlayerId(playerDto.getId());
        player.setNickname(playerDto.getNickname());
        Resources resources = new Resources();
        player.setResources(resources);
        playerRepository.save(player);
        session.getPlayers().add(player);
        gameSessionRepository.save(session);
        return session;
    }

    /**
     * Запускает игровую сессию.
     */
    public GameSession startGameSession(UUID sessionId) {
        // Инициализация игрового состояния
        GameSession session = getGameSession(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        GameSession newGameSession = gameSessionRepository.save(ruleService.initializeGame(session));
        for(Player player: newGameSession.getPlayers()) {
            messageGameService.sendMessageToPlayer(newGameSession.getSessionId(), player.getPlayerId(), "start");
        }
        return newGameSession;
    }

    /**
     * Обрабатывает действие игрока.
     */
    public GameSession performAction(UUID sessionId, UUID playerId, String actionType, Object actionData) {
        GameSession session = getGameSession(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        //gameService.processAction(session, playerId, actionType, actionData);
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
