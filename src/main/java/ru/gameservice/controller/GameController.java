package ru.gameservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gameservice.dto.LocationDto;
import ru.gameservice.dto.PlayerDto;
import ru.gameservice.entity.GameSession;
import ru.gameservice.entity.cards.Card;
import ru.gameservice.entity.locations.Location;
import ru.gameservice.service.GameManagerService;
import ru.gameservice.service.GameSessionService;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления игровыми сессиями.
 */
@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameManagerService gameManagerService;
    @Autowired
    private GameSessionService gameSessionService;

    /**
     *  Создает новую игровую сессию.
     * @param gameSessionId
     */
    @PostMapping
    public void createGameSession(@RequestBody UUID gameSessionId) {
        gameSessionService.createGameSession();
    }

    /**
     * Запускает игровую сессию.
     * @param sessionId
     * @return
     */
    @PostMapping("/{sessionId}/start")
    public ResponseEntity<GameSession> startGameSession(@PathVariable UUID sessionId) {
        GameSession session = gameManagerService.startGameSession(sessionId);
        return ResponseEntity.ok(session);
    }

    /**
     * Присоединяет игрока к игровой сессии.
     * @param pin
     * @param playerDto
     * @return
     */
    @PostMapping("/{pin}/join")
    public ResponseEntity<GameSession> joinGameSession(@PathVariable String pin, @RequestBody PlayerDto playerDto) {
        GameSession session = gameManagerService.addPlayerToSession(pin, playerDto);
        return ResponseEntity.ok(session);
    }

    /**
     * Получает состояние игровой сессии.
     * @param sessionId
     * @return
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<GameSession> getGameSession(@PathVariable UUID sessionId) {
        GameSession session =  gameSessionService.getGameSession(sessionId);
        return ResponseEntity.ok(session);
    }

    /**
     * Получаем список карт с поляны
     * @param gameId
     * @param playerId
     * @return
     */
    @GetMapping("/{gameId}/{playerId}/getCardsInMeadow")
    public List<Card> getBuildCardsInMeadowAction(@PathVariable UUID gameId, @PathVariable UUID playerId) {
        return gameManagerService.getCardsInMeadow(gameId, playerId);
    }

    @GetMapping("/{gameId}/{playerId}/{cardId}/getCheckFreeBuild")
    public ResponseEntity<List<Card>> checkFreeBuild(@PathVariable UUID gameId, @PathVariable UUID playerId, @PathVariable UUID cardId) {
        List<Card> freeBuildCards =  gameManagerService.getListCardsInFreeBuild(gameId, playerId, cardId);
        return ResponseEntity.ok(freeBuildCards);
    }

    @GetMapping("/{gameId}/{playerId}/getWorkersSlot")
    public ResponseEntity<LocationDto> getWorkersSlot(@PathVariable UUID gameId, @PathVariable UUID playerId) {
        LocationDto workersSlot =  gameManagerService.getWorkersSlot(gameId, playerId);
        System.out.println(workersSlot);
        return ResponseEntity.ok(workersSlot);
    }
}
