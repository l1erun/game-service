package ru.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gameservice.dto.PlayerDto;
import ru.gameservice.entity.*;
import ru.gameservice.entity.cards.Card;
import ru.gameservice.enums.Season;
import ru.gameservice.repository.PlayerRepository;

import java.util.*;

/**
 * Сервис для применения правил игры.
 */
@Service
public class RuleService {
    @Autowired
    private AuthService authService;
    @Autowired
    private WebClient webClient;
    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Инициализирует игровую сессию перед началом игры.
     */
    public GameSession initializeGame(GameSession session) {
        List<Card> cardList = getAllCards();
        Collections.shuffle(cardList);
        GameState gameState = new GameState();
        gameState.setCurrentTurn(0);
        gameState.setEvents(getAllEvents());
        gameState.setLocations(getAllBaseLocation());
        gameState.setMeadowCard(drawCards(cardList, 8));
        for (Player player : session.getPlayers()) {
            player.setHand(drawCards(cardList, 5));
            player.setCurrentSeason(Season.WINTER);
        }
        gameState.setDeck(cardList);
        session.setGameState(gameState);
        session.setCreatedAt(System.currentTimeMillis());
        return session;
    }

    /**
     * Завершает ход игрока и переключает на следующего.
     */
    public void endPlayerTurn(GameSession session, UUID playerId) {
        // Логика завершения хода и переключения на следующего игрока
    }

    /**
     * Проверяет победные условия.
     */
    public boolean checkWinConditions(GameSession session) {
        // Проверка условий победы
        return false;
    }

    private Player getPlayerInId(UUID playerId) {
        String jwtToken = authService.authenticate();
        PlayerDto playerDto= webClient.get()
                .uri("http://localhost:8080/players/" + playerId + "/player-response") // Endpoint аутентификации
                .header("Authorization", jwtToken)
                .header("Accept", "application/json") // Указываем, что ожидаем JSON
//                .bodyValue() // Передаем учетные данные в теле запроса
                .retrieve()
                .bodyToMono(PlayerDto.class)
                .block();
        Resources resources = new Resources();
        resources.setBerries(0);
        resources.setResin(0);
        resources.setPebbles(0);
        resources.setTwigs(0);

        Player player = new Player();
        player.setPlayerId(playerDto.getId());
        player.setNickname(playerDto.getNickname());
        player.setAvatarUrl(playerDto.getAvatarUrl());
        player.setCity(new ArrayList<>());
        player.setHand(new ArrayList<>());
        player.setPoints(0);
        player.setResources(resources);
        return playerRepository.save(player);
    }

    public List<Card> getAllCards(){
        String jwtToken = authService.authenticate();
        List<Card> cards= webClient.get()
                .uri("http://localhost:8080/cards") // Endpoint аутентификации
                .header("Authorization", jwtToken)
                .header("Accept", "application/json") // Указываем, что ожидаем JSON
//                .bodyValue() // Передаем учетные данные в теле запроса
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Card>>() {})
                .block();
        return cards;
    }

    public List<Event> getAllEvents(){
        String jwtToken = authService.authenticate();
        return webClient.get()
                .uri("http://localhost:8080/cards/events") // Endpoint аутентификации
                .header("Authorization", jwtToken)
                .header("Accept", "application/json") // Указываем, что ожидаем JSON
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Event>>() {})
                .block();
    }

    public List<Location> getAllBaseLocation(){
        String jwtToken = authService.authenticate();
        return webClient.get()
                .uri("http://localhost:8080/cards/baseLocations") // Endpoint аутентификации
                .header("Authorization", jwtToken)
                .header("Accept", "application/json") // Указываем, что ожидаем JSON
//                .bodyValue() // Передаем учетные данные в теле запроса
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Location>>() {})
                .block();
    }

    // Метод для получения 5-10 карт и удаления их из исходного списка
    public static List<Card> drawCards(List<Card> cardList, int count) {
        if (count > cardList.size()) {
            throw new IllegalArgumentException("Недостаточно карт в списке для выбора " + count + " карт.");
        }
        List<Card> selectedCards = new ArrayList<>(cardList.subList(0, count)); // Получаем первые count карт
        cardList.subList(0, count).clear(); // Удаляем эти карты из исходного списка
        return selectedCards;
    }

    // Другие методы, связанные с правилами игры
}
