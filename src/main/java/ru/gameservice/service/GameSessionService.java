package ru.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gameservice.entity.GameSession;
import ru.gameservice.entity.GameState;
import ru.gameservice.entity.Player;
import ru.gameservice.entity.cards.Card;
import ru.gameservice.enums.Season;
import ru.gameservice.repository.GameSessionRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class GameSessionService {

    @Autowired
    public GameSessionRepository gameSessionRepository;
    @Autowired
    private WebClientService webClientService;

    /**
     * Создает новую игровую сессию.
     *
     * @return
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
     * Получить gameSession
     * @param gameSessionId
     * @return
     */
    public GameSession getGameSession(UUID gameSessionId){
        return gameSessionRepository.findById(gameSessionId)
                .orElseThrow(() -> new NoSuchElementException("Сессия с id: " + gameSessionId + "не найдена"));
    }

    /**
     * Сохранить gameSession
     * @param gameSession
     */
    public void saveGameSession(GameSession gameSession){
        gameSessionRepository.save(gameSession);
    }

    /**
     * Инициализирует игровую сессию перед началом игры.
     * @param sessionId
     * @return
     */
    public GameSession initializeGameSession(UUID sessionId) {
        GameSession session = getGameSession(sessionId);
        List<Card> cardList = webClientService.getAllCards();
        GameState gameState = new GameState();
        gameState.setCurrentTurn(0);
        gameState.setEvents(webClientService.getAllEvents());
        gameState.setBaseLocations(webClientService.getAllBaseLocations());
        gameState.setForestLocations(StaticService.drawCards(webClientService.getAllForestLocations(), 4 ));
        gameState.setMeadowCard(StaticService.drawCards(cardList, 8));
        for (Player player : session.getPlayers()) {
            player.setHand(StaticService.drawCards(cardList, 5));
            player.setCurrentSeason(Season.WINTER);
        }
        gameState.setDeck(cardList);
        session.setGameState(gameState);
        session.setCreatedAt(System.currentTimeMillis());
        return gameSessionRepository.save(session);
    }

    /**
     * Поиск доступных gameSession по pinCode
     * @param pin
     * @return
     */
    public GameSession findGameSessionInPinCode(String pin){
        return StreamSupport.stream(gameSessionRepository.findAll().spliterator(), false)
                .filter(gameSession -> gameSession.getPin().equals(pin))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Сессия с PIN-кодом " + pin + " не найдена"));
    }

    public Player findPlayerInGameSession(GameSession gameSession, UUID playerId){
        return gameSession.getPlayers().stream()
                .filter(player -> player.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Пользователь с id: " + playerId + " не найден в" +
                        " игровой сессии: " + gameSession));
    }

}
