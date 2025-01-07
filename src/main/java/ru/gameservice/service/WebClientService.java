package ru.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gameservice.dto.PlayerActionDto;
import ru.gameservice.entity.Events.Event;
import ru.gameservice.entity.cards.Card;
import ru.gameservice.entity.locations.Location;
import ru.gameservice.websocket.SessionRegistry;

import java.util.List;
import java.util.UUID;

@Service
public class WebClientService {

    @Autowired
    private AuthService authService;
    @Autowired
    private WebClient webClient;

    public void saveRegistryConnectionWebSocket(UUID gameId, String sessionId, PlayerActionDto action){
        UUID playerId = action.getPlayerId();
        if (playerId != null) {
            SessionRegistry.addPlayerSession(gameId, playerId, sessionId);
            System.out.println("Сохранено соответствие playerId " + playerId + " и sessionId " + sessionId + " для игры " + gameId);
        } else {
            SessionRegistry.addGameBoardSession(gameId, sessionId);
            System.out.println("Сохранена сессия игрового поля для игры " + gameId);
        }
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

    public List<Location> getAllBaseLocations(){
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

    public List<Location> getAllForestLocations(){
        String jwtToken = authService.authenticate();
        return webClient.get()
                .uri("http://localhost:8080/cards/forestLocations") // Endpoint аутентификации
                .header("Authorization", jwtToken)
                .header("Accept", "application/json") // Указываем, что ожидаем JSON
//                .bodyValue() // Передаем учетные данные в теле запроса
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Location>>() {})
                .block();
    }
}
