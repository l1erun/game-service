package ru.gameservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.Controller;
import ru.gameservice.entity.GameState;
import ru.gameservice.entity.PlayerAction;
import ru.gameservice.service.ActionService;
import ru.gameservice.service.GameService;
import ru.gameservice.service.MessageGameService;
import ru.gameservice.websocket.SessionRegistry;

import java.util.UUID;

@Controller
public class GameSessionController {

    private final GameService gameService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private final MessageGameService messageGameService;

    public GameSessionController(GameService gameService, MessageGameService messageGameService) {
        this.gameService = gameService;
        this.messageGameService = messageGameService;
    }

    @MessageMapping("/{gameId}/action")
    public void handlePlayerAction(@DestinationVariable UUID gameId, PlayerAction action, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        UUID playerId = action.getPlayerId();
        if (playerId != null) {
            SessionRegistry.addPlayerSession(gameId, playerId, sessionId);
            System.out.println("Сохранено соответствие playerId " + playerId + " и sessionId " + sessionId + " для игры " + gameId);
        } else {
            SessionRegistry.addGameBoardSession(gameId, sessionId);
            System.out.println("Сохранена сессия игрового поля для игры " + gameId);
        }
    }

    @MessageMapping("/{gameId}/getGameState")
    public void handleGameState(@DestinationVariable UUID gameId) {
        actionService.getGameState(gameId);
    }

    @MessageMapping("/{gameId}/{playerId}/getDataPlayer")
    public void handlePlayerAction(@DestinationVariable UUID gameId, @DestinationVariable UUID playerId) {
        actionService.getPlayer(gameId, playerId);
    }

    @MessageMapping("/{gameId}/{playerId}/goToSeason")
    public void handleGoToSeason(@DestinationVariable UUID gameId, @DestinationVariable UUID playerId) {
        gameService.processGoToSeasonAction(gameId, playerId);
    }

    @MessageMapping("/{gameId}/{playerId}/{cardId}/cardAction")
    public void handleAction(@DestinationVariable UUID gameId, @DestinationVariable UUID playerId, @DestinationVariable UUID cardId) {
        gameService.processAction(gameId, playerId, cardId);
    }
}
