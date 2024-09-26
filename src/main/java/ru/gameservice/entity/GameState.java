package ru.gameservice.entity;

import lombok.Data;
import ru.gameservice.enums.Season;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Текущее состояние игры. Живет в течение сессии.
 */
@Data
public class GameState implements Serializable {
    private int currentTurn; // Номер текущего хода

    private UUID activePlayerId; // Идентификатор текущего игрока

    private BoardState boardState; // Состояние игрового поля

    private List<Card> deck; // Оставшиеся в колоде карты

    private List<Card> discardPile; // Карты в сбросе

    private Season currentSeason; // Текущий сезон игры

    // Другие необходимые поля
}
