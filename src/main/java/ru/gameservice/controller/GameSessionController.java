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
    private final MessageGameService messageGameService;

    public GameSessionController(GameService gameService, MessageGameService messageGameService) {
        this.gameService = gameService;
        this.messageGameService = messageGameService;
    }

    @MessageMapping("/{gameId}/action")
    public void handlePlayerAction(@DestinationVariable String gameId, PlayerAction action, SimpMessageHeaderAccessor headerAccessor) {
        // Получаем sessionId
        String sessionId = headerAccessor.getSessionId();
        UUID gameUUID = UUID.fromString(gameId);
        UUID playerId = action.getPlayerId();

        // Если playerId != null, это игрок
        if (playerId != null) {
            SessionRegistry.addPlayerSession(gameUUID, playerId, sessionId);
            System.out.println("Сохранено соответствие playerId " + playerId + " и sessionId " + sessionId + " для игры " + gameId);
        } else {
            SessionRegistry.addGameBoardSession(gameUUID, sessionId);
            System.out.println("Сохранена сессия игрового поля для игры " + gameId);
        }

        gameService.processAction(gameUUID, playerId, action.getActionType(), action.getActionData());

        GameState updatedState = gameService.getUpdatedGameState(gameUUID);
        messageGameService.sendMessageToGame(gameUUID, updatedState);
    }
}
