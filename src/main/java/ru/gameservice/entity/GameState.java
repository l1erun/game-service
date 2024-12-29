package ru.gameservice.entity;

import lombok.*;
import ru.gameservice.entity.cards.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Текущее состояние игры. Живет в течение сессии.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@RedisHash("GameState")
public class GameState implements Serializable {
    private int currentTurn; // Номер текущего хода
    private UUID activePlayerId; // Идентификатор текущего игрока
    private List<Event> events = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();
    private List<Card> meadowCard = new ArrayList<>();
    private List<Card> deck = new ArrayList<>(); // Оставшиеся в колоде карты
    private List<Card> discardPile = new ArrayList<>(); // Карты в сбросе

    // Другие необходимые поля
}
