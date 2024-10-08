package ru.gameservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.gameservice.entity.GameSession;
import ru.gameservice.entity.Player;
import ru.gameservice.service.ActionService;
import ru.gameservice.service.GameService;

import java.util.UUID;

/**
 * Контроллер для управления игровыми сессиями.
 */
@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private ActionService actionService;

    /**
     * Создает новую игровую сессию.
     */
    @PostMapping
    public GameSession createGameSession() {
        return actionService.createGameSession();
    }

    /**
     * Получает состояние игровой сессии.
     */
    @GetMapping("/{sessionId}")
    public GameSession getGameSession(@PathVariable UUID sessionId) {
        return actionService.getGameSession(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
    }

    /**
     * Присоединяет игрока к игровой сессии.
     */
    @PostMapping("/{sessionId}/join")
    public GameSession joinGameSession(@PathVariable UUID sessionId, @RequestBody Player player) {
        return actionService.addPlayerToSession(sessionId, player);
    }

    /**
     * Запускает игровую сессию.
     */
    @PostMapping("/{sessionId}/start")
    public GameSession startGameSession(@PathVariable UUID sessionId) {
        return actionService.startGameSession(sessionId);
    }

    /**
     * Выполняет действие игрока.
     */
    @PostMapping("/{sessionId}/action")
    public GameSession performAction(@PathVariable UUID sessionId,
                                     @RequestParam UUID playerId,
                                     @RequestParam String actionType,
                                     @RequestBody Object actionData) {
        return actionService.performAction(sessionId, playerId, actionType, actionData);
    }

    /**
     * Завершает ход игрока.
     */
    @PostMapping("/{sessionId}/endTurn")
    public GameSession endTurn(@PathVariable UUID sessionId, @RequestParam UUID playerId) {
        return actionService.endTurn(sessionId, playerId);
    }
}
