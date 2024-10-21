package ru.gameservice.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Информация об игроке в рамках игровой сессии.
 */
@Getter
@Setter
@NoArgsConstructor
@RedisHash("Player")
public class Player implements Serializable {
    @Id
    private UUID playerId; // Идентификатор игрока
    private String nickname; // Отображаемое имя игрока
    private String avatarUrl;

    private List<Card> hand = new ArrayList<>(); // Карты на руках

    private List<Card> city = new ArrayList<>(); // Построенные карты в городе

    private Resources resources; // Ресурсы игрока

    private int points; // Текущее количество очков

    // Другие необходимые поля
}
