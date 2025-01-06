package ru.gameservice.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.Controller;
import ru.gameservice.dto.PlayerActionDto;
import ru.gameservice.service.GameManagerService;
import ru.gameservice.service.WebClientService;

import java.util.Objects;
import java.util.UUID;

@Controller
public class GameSessionController {
    private final WebClientService webClientService;
    private final GameManagerService gameManagerService;

    public GameSessionController(WebClientService webClientService, GameManagerService gameManagerService) {
        this.webClientService = webClientService;
        this.gameManagerService = gameManagerService;
    }

    @MessageMapping("/{gameId}/connection")
    public void handlePlayerAction(@DestinationVariable UUID gameId, PlayerActionDto action, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = Objects.requireNonNull(headerAccessor.getSessionId());
        webClientService.saveRegistryConnectionWebSocket(gameId, sessionId, action);
    }

    @MessageMapping("/{gameId}/getGameState")
    public void handleGameState(@DestinationVariable UUID gameId) {
        gameManagerService.sendGameStateInGameBoard(gameId);
    }

    @MessageMapping("/{gameId}/{playerId}/getDataPlayer")
    public void handlePlayerAction(@DestinationVariable UUID gameId, @DestinationVariable UUID playerId) {
        gameManagerService.sendDataPlayerInPlayer(gameId, playerId);
    }

    @MessageMapping("/{gameId}/{playerId}/goToSeason")
    public void handleGoToSeason(@DestinationVariable UUID gameId, @DestinationVariable UUID playerId) {
        gameManagerService.movePlayerToNextSeason(gameId, playerId);
    }

    @MessageMapping("/{gameId}/{playerId}/{cardId}/buildCardFree")
    public void handleFreeBuildCard(@DestinationVariable UUID gameId, @DestinationVariable UUID playerId, @DestinationVariable UUID cardId) {
        gameManagerService.freeBuildCard(gameId, playerId, cardId);
    }

    @MessageMapping("/{gameId}/{playerId}/{cardId}/buildCardWithResources")
    public void handleBuildResourcesBuildCard(@DestinationVariable UUID gameId, @DestinationVariable UUID playerId, @DestinationVariable UUID cardId) {
        gameManagerService.resourcesBuildCard(gameId, playerId, cardId);
    }

    @MessageMapping("/{gameId}/{playerId}/{locationId}/sendWorkerToLocation")
    public void handleSetWorkerToLocation(@DestinationVariable UUID gameId, @DestinationVariable UUID playerId, @DestinationVariable UUID locationId) {
        gameManagerService.setWorkerToLocation(gameId, playerId, locationId);
    }
}
