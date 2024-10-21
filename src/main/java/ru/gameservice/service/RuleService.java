package ru.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gameservice.dto.GameSessionRequest;
import ru.gameservice.dto.PlayerDto;
import ru.gameservice.entity.*;
import ru.gameservice.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.UUID;

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
    public void initializeGame(GameSession session, GameSessionRequest gameSessionRequest) {
        for (UUID userId : gameSessionRequest.getPlayers()) {
            session.getPlayers().add(getPlayerInId(userId));
        }
        session.setCreatedAt(System.currentTimeMillis());
        session.setGameState(new GameState());
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
        System.out.println(jwtToken);
        PlayerDto playerDto= webClient.get()
                .uri("http://localhost:8080/players/" + playerId + "/player-response") // Endpoint аутентификации
                .header("Authorization", jwtToken)
                .header("Accept", "application/json") // Указываем, что ожидаем JSON
//                .bodyValue() // Передаем учетные данные в теле запроса
                .retrieve()
                .bodyToMono(PlayerDto.class)
                .block();
//        System.out.println(playerRequest);
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

    // Другие методы, связанные с правилами игры
}
