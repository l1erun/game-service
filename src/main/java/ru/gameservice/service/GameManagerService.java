package ru.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gameservice.dto.LocationDto;
import ru.gameservice.dto.MessageDto;
import ru.gameservice.dto.PlayerDto;
import ru.gameservice.entity.GameSession;
import ru.gameservice.entity.Player;
import ru.gameservice.entity.cards.Card;
import ru.gameservice.entity.locations.Location;
import ru.gameservice.enums.Season;

import java.util.*;

/**
 * Основной сервис для распределения и управления всеми сервисами
 */
@Service
public class GameManagerService {

    @Autowired
    public MessageGameService messageGameService;
    @Autowired
    public GameSessionService gameSessionService;
    @Autowired
    public PlayerService playerService;
    @Autowired
    public CardService cardService;

    /**
     * Запускает игровую сессию.
     *
     * @param sessionId
     * @return
     */
    public GameSession startGameSession(UUID sessionId) {
        GameSession session = gameSessionService.initializeGameSession(sessionId);
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("start");
        for (Player player : session.getPlayers()) {
            messageGameService.sendMessageToPlayer(session.getSessionId(), player.getPlayerId(), messageDto);
        }
        return session;
    }

    /**
     * Добавляет игрока в игровую сессию.
     *
     * @param pin
     * @param playerDto
     * @return
     */
    public GameSession addPlayerToSession(String pin, PlayerDto playerDto) {
        GameSession gameSession = gameSessionService.findGameSessionInPinCode(pin);
        Player player = playerService.createNewPlayer(playerDto);
        gameSession.getPlayers().add(player);
        gameSessionService.saveGameSession(gameSession);
        return gameSession;
    }

    /**
     * Получаем список карт с поляны
     *
     * @param sessionId
     * @param playerId
     * @return
     */
    public List<Card> getCardsInMeadow(UUID sessionId, UUID playerId) {
        GameSession session = gameSessionService.getGameSession(sessionId);
        return session.getGameState().getMeadowCard();
    }

    /**
     * Получить список карт для бесплатного строительства
     *
     * @param sessionId
     * @param playerId
     * @param cardId
     * @return
     */
    public List<Card> getListCardsInFreeBuild(UUID sessionId, UUID playerId, UUID cardId) {
        GameSession session = gameSessionService.getGameSession(sessionId);
        Player player = gameSessionService.findPlayerInGameSession(session, playerId);
        Card card = cardService.findCardIdOnCardsList(session.getGameState().getMeadowCard(), cardId);
        if (card == null) {
            card = cardService.findCardIdOnCardsList(player.getHand(), cardId);
        }
        List<Card> cards = new ArrayList<>();
        for (Card cardCity : player.getCity()) {
            if (cardService.checkFreeBuildCardInCardPlayer(card, cardCity)) {
                cards.add(cardCity);
            }
        }
        return cards;
    }

    /**
     * Отпарвляет данные игрового поля игровому полю
     *
     * @param sessionId
     */
    public void sendGameStateInGameBoard(UUID sessionId) {
        GameSession gameSession = gameSessionService.getGameSession(sessionId);
        messageGameService.sendMessageToGame(sessionId, gameSession.getGameState());
    }

    public void sendDataPlayerInPlayer(UUID sessionId, UUID playerId) {
        GameSession gameSession = gameSessionService.getGameSession(sessionId);
        Player player = gameSessionService.findPlayerInGameSession(gameSession, playerId);
        System.out.println(player);
        messageGameService.sendMessageToPlayer(sessionId, playerId, player);
    }

    /**
     * Перевод игрока в следующий сезон
     *
     * @param sessionId
     * @param playerId
     */
    public void movePlayerToNextSeason(UUID sessionId, UUID playerId) {
        GameSession gameSession = gameSessionService.getGameSession(sessionId);
        Player player = gameSessionService.findPlayerInGameSession(gameSession, playerId);
        // todo добавить обработку смены сезона
        switch (player.getCurrentSeason()) {
            case WINTER -> player.setCurrentSeason(Season.SPRING);
            case SPRING -> player.setCurrentSeason(Season.SUMMER);
            case SUMMER -> player.setCurrentSeason(Season.AUTUMN);
            case AUTUMN -> player.setCurrentSeason(Season.FINISH);
            default -> throw new IllegalStateException("Неизвестное состояние сезона: " + player.getCurrentSeason());
        }
        player.setFlagActivePlayer(false);
        gameSessionService.saveGameSession(gameSession);
        messageGameService.sendMessageToPlayer(sessionId, playerId, player);
    }

    public void freeBuildCard(UUID sessionId, UUID playerId, UUID cardId) {
        GameSession gameSession = gameSessionService.getGameSession(sessionId);
        Player player = gameSessionService.findPlayerInGameSession(gameSession, playerId);
        boolean flagHandCard = player.getHand().stream()
                .anyMatch(card -> card.getId().equals(cardId));
        boolean flagMeadowCard = gameSession.getGameState().getMeadowCard().stream()
                .anyMatch(card -> card.getId().equals(cardId));
        if (flagHandCard) {
            Card card = cardService.findCardIdOnCardsList(player.getHand(), cardId);
            for (Card cardCity : player.getCity()) {
                if (cardService.checkFreeBuildCardInCardPlayer(card, cardCity)) {
                    player.getCity().add(card);
                    player.getHand().remove(card);
                    cardCity.setLocker(true);
                    gameSessionService.saveGameSession(gameSession);
                    messageGameService.sendMessageToPlayer(sessionId, playerId, player);
                    break;
                }
            }
        } else if (flagMeadowCard) {
            Card card = cardService.findCardIdOnCardsList(gameSession.getGameState().getMeadowCard(), cardId);
            for (Card cardCity : player.getCity()) {
                if (cardService.checkFreeBuildCardInCardPlayer(card, cardCity)) {
                    player.getCity().add(card);
                    Card newCard = gameSession.getGameState().getDeck().removeFirst();
                    gameSession.getGameState().getMeadowCard().remove(card);
                    gameSession.getGameState().getMeadowCard().add(newCard);
                    cardCity.setLocker(true);
                    gameSessionService.saveGameSession(gameSession);
                    messageGameService.sendMessageToPlayer(sessionId, playerId, player);
                    messageGameService.sendMessageToGame(sessionId, gameSession.getGameState());
                }
            }
        }
    }

    public void resourcesBuildCard(UUID sessionId, UUID playerId, UUID cardId) {
        GameSession gameSession = gameSessionService.getGameSession(sessionId);
        Player player = gameSessionService.findPlayerInGameSession(gameSession, playerId);
        boolean flagHandCard = player.getHand().stream()
                .anyMatch(card -> card.getId().equals(cardId));
        boolean flagMeadowCard = gameSession.getGameState().getMeadowCard().stream()
                .anyMatch(card -> card.getId().equals(cardId));
        if (flagHandCard) {
            Card card = cardService.findCardIdOnCardsList(player.getHand(), cardId);
            if (playerService.checkResources(player.getResources(), card.getCost())) {
                player.getCity().add(card);
                player.getHand().remove(card);
                gameSessionService.saveGameSession(gameSession);
                messageGameService.sendMessageToPlayer(sessionId, playerId, player);
            }
        } else if (flagMeadowCard) {
            Card card = cardService.findCardIdOnCardsList(gameSession.getGameState().getMeadowCard(), cardId);
            if (playerService.checkResources(player.getResources(), card.getCost())) {
                player.getCity().add(card);
                Card newCard = gameSession.getGameState().getDeck().removeFirst();
                gameSession.getGameState().getMeadowCard().remove(card);
                gameSession.getGameState().getMeadowCard().add(newCard);
                gameSessionService.saveGameSession(gameSession);
                messageGameService.sendMessageToPlayer(sessionId, playerId, player);
                messageGameService.sendMessageToGame(sessionId, gameSession.getGameState());
            }
        }
    }

    public LocationDto getWorkersSlot(UUID sessionId, UUID playerId) {
        GameSession gameSession = gameSessionService.getGameSession(sessionId);
        LocationDto locationDto = new LocationDto();
        for (Location baseLocation : gameSession.getGameState().getBaseLocations()) {
            if (baseLocation.isUniq() && !baseLocation.getOccupiedBy().isEmpty()) {

            } else if (!baseLocation.getOccupiedBy().contains(playerId) && baseLocation.getWorkerSlots() - baseLocation.getOccupiedBy().size() > 0) {
                locationDto.getBaseLocation().add(baseLocation);
            }
        }
        for (Location forestLocation : gameSession.getGameState().getForestLocations()) {
            if (forestLocation.isUniq() && !forestLocation.getOccupiedBy().isEmpty()) {

            } else if (!forestLocation.getOccupiedBy().contains(playerId) && forestLocation.getWorkerSlots() - forestLocation.getOccupiedBy().size() > 0) {
                locationDto.getForestLocation().add(forestLocation);
            }
        }
        for (Location userLocation : gameSession.getGameState().getUserLocation()) {
            if (userLocation.isUniq() && !userLocation.getOccupiedBy().isEmpty()) {

            } else if (!userLocation.getOccupiedBy().contains(playerId) && userLocation.getWorkerSlots() - userLocation.getOccupiedBy().size() > 0) {
                locationDto.getUserLocation()
                        .computeIfAbsent(userLocation.getId().toString(), key -> new ArrayList<>())
                        .add(userLocation);
            }
        }
        return locationDto;
    }

    public void setWorkerToLocation(UUID sessionId, UUID playerId, UUID locationId) {
        GameSession gameSession = gameSessionService.getGameSession(sessionId);
        Player player = gameSessionService.findPlayerInGameSession(gameSession, playerId);
        if (player.getWorkers() > 0) {
            Optional<Location> base = gameSession.getGameState().getBaseLocations().stream()
                    .filter(location -> location.getId().equals(locationId))
                    .findFirst();
            if (base.isPresent()) {
                Location location = base.get();
                location.getOccupiedBy().add(playerId);
                player.setWorkers(player.getWorkers() - 1);
//                return;
            }

            Optional<Location> forest = gameSession.getGameState().getForestLocations().stream()
                    .filter(location -> location.getId().equals(locationId))
                    .findFirst();
            if (forest.isPresent()) {
                Location location = forest.get();
                location.getOccupiedBy().add(playerId);
                player.setWorkers(player.getWorkers() - 1);
//                return;
            }

            Optional<Location> user = gameSession.getGameState().getUserLocation().stream()
                    .filter(location -> location.getId().equals(locationId))
                    .findFirst();
            if (user.isPresent()) {
                Location location = user.get();
                location.getOccupiedBy().add(playerId);
                player.setWorkers(player.getWorkers() - 1);
//                return;
            }
            gameSessionService.saveGameSession(gameSession);
            messageGameService.sendMessageToPlayer(sessionId, playerId, player);
            messageGameService.sendMessageToGame(sessionId, gameSession);
//            throw new NoSuchElementException("Локация с id: " + locationId + " не найдена");
        }

    }
}
