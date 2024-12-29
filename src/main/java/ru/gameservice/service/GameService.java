package ru.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gameservice.entity.*;
import ru.gameservice.entity.cards.Card;
import ru.gameservice.enums.Season;
import ru.gameservice.repository.GameSessionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameService {
    @Autowired
    public ActionService actionService;
    @Autowired
    public MessageGameService messageGameService;
    @Autowired
    public GameSessionRepository gameSessionRepository;

    /**
     * Постройка карт
     *
     * @param gameId
     * @param playerId
     * @param cardId
     */
    public void processAction(UUID gameId, UUID playerId, UUID cardId) {
        Optional<GameSession> optionalGameSession = actionService.getGameSession(gameId);
        if (optionalGameSession.isPresent()) {
            for (Player player : optionalGameSession.get().getPlayers()) {
                if (player.getPlayerId().equals(playerId)) {
                    for (Card card : player.getHand()) {
                        if (card.getId().equals(cardId)) {
                            // нашли карту
                            if (checkResources(player.getResources(), card.getCost())) { //проверка по количеству ресурсов
                                //  проверить возможность постройки бесплатно
                                player.getCity().add(card);
                                player.getHand().remove(card);
                                gameSessionRepository.save(optionalGameSession.get());
                                messageGameService.sendMessageToPlayer(gameId, playerId, player);
                            } else {
                                messageGameService.sendMessageToPlayer(gameId, playerId, "Ошибка");
                            }
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Игровая сессия с ID " + gameId + " не существует");
        }

    }

    private boolean checkResources(Resources playerResources, Cost costCard) {
        if (playerResources.getBerries() >= costCard.getBerries()
                && playerResources.getResin() >= costCard.getResin()
                && playerResources.getPebbles() >= costCard.getPebbles()
                && playerResources.getTwigs() >= costCard.getTwigs()) {
            playerResources.setBerries(playerResources.getBerries() - costCard.getBerries());
            playerResources.setResin(playerResources.getResin() - costCard.getResin());
            playerResources.setPebbles(playerResources.getPebbles() - costCard.getPebbles());
            playerResources.setTwigs(playerResources.getTwigs() - costCard.getTwigs());
            return true;
        }
        return false;
    }

    private void processPlayerAction(UUID gameId, UUID playerId, String actionType, Object actionData) {
        // Логика обработки действия игрока
    }

    private void processGameBoardAction(UUID gameId, String actionType, Object actionData) {
        // Логика обработки действия игрового поля
    }

    /**
     * перевод игрока в следующую суссию
     *
     * @param gameId
     * @param playerId
     */
    public void processGoToSeasonAction(UUID gameId, UUID playerId) {
        Optional<GameSession> optionalGameSession = actionService.getGameSession(gameId);
        if (optionalGameSession.isPresent()) {
            for (Player player : optionalGameSession.get().getPlayers()) {
                if (player.getPlayerId().equals(playerId)) {
                    // Изменяем состояние игрока напрямую
                    switch (player.getCurrentSeason()) {
                        case WINTER -> player.setCurrentSeason(Season.SPRING);
                        case SPRING -> player.setCurrentSeason(Season.SUMMER);
                        case SUMMER -> player.setCurrentSeason(Season.AUTUMN);
                        case AUTUMN -> player.setCurrentSeason(Season.FINISH);
                    }
                    player.setFlagActivePlayer(false);
                    gameSessionRepository.save(optionalGameSession.get());
                    messageGameService.sendMessageToPlayer(gameId, playerId, player);
                    break;
                }
            }
//            messageGameService.sendMessageToGame(gameId, optionalGameSession.get().getGameState());
        } else {
            throw new IllegalArgumentException("Игровая сессия с ID " + gameId + " не существует");
        }
    }

    public GameState getUpdatedGameState(UUID gameId) {
        // Получение обновленного состояния игры
        return new GameState();
    }
}
