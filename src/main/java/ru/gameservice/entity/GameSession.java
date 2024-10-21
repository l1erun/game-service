package ru.gameservice.entity;

//import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Игровая сессия. Хранится в Redis.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@RedisHash("GameSession")
public class GameSession implements Serializable {
    @Id
    private UUID sessionId; // Уникальный идентификатор игровой сессии

    private List<Player> players = new ArrayList<>(); // Список игроков в сессии

    private GameState gameState; // Текущее состояние игры

    private long createdAt; // Время создания сессии в миллисекундах
}
