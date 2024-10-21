package ru.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gameservice.dto.AuthResponse;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private WebClient webClient;
    private final String username = "test";
    private final String password = "root";

    public String authenticate() {
        Map<String, String> authRequest = new HashMap<>();
        authRequest.put("username", username);
        authRequest.put("password", password);

        // Выполняем POST-запрос для получения токена
        AuthResponse authResponse = webClient.post()
                .uri("http://localhost:8080/auth/login") // Endpoint аутентификации
                .bodyValue(authRequest) // Передаем учетные данные в теле запроса
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block(); // Получаем синхронный ответ

        return authResponse.getType() + " " + authResponse.getToken();
    }
}
