package ru.gameservice.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import ru.gameservice.enums.Season;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Текущее состояние игры. Живет в течение сессии.
 */
@Getter
@Setter
@NoArgsConstructor
@RedisHash("GameState")
public class GameState implements Serializable {
    private int currentTurn; // Номер текущего хода

    private UUID activePlayerId; // Идентификатор текущего игрока

    private BoardState boardState; // Состояние игрового поля

    private List<Card> deck = new ArrayList<>(); // Оставшиеся в колоде карты

    private List<Card> discardPile = new ArrayList<>(); // Карты в сбросе

    private Season currentSeason; // Текущий сезон игры

    // Другие необходимые поля
}
