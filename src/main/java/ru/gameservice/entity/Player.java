package ru.gameservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Информация об игроке в рамках игровой сессии.
 */
@Data
public class Player implements Serializable {
    private UUID playerId; // Идентификатор игрока

    private String nickname; // Отображаемое имя игрока

    private List<Card> hand; // Карты на руках

    private List<Card> city; // Построенные карты в городе

    private Resources resources; // Ресурсы игрока

    private int points; // Текущее количество очков

    // Другие необходимые поля
}
