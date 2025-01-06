package ru.gameservice.entity;

import lombok.*;
import ru.gameservice.entity.Events.Event;
import ru.gameservice.entity.cards.Card;
import ru.gameservice.entity.locations.Forest;
import ru.gameservice.entity.locations.Location;

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
    private List<Location> baseLocations = new ArrayList<>();
    private List<Location> forestLocations = new ArrayList<>();
    private List<Location> userLocation = new ArrayList<>();
    private List<Card> meadowCard = new ArrayList<>();
    private List<Card> deck = new ArrayList<>(); // Оставшиеся в колоде карты
    private List<Card> discardPile = new ArrayList<>(); // Карты в сбросе

    // Другие необходимые поля
}
