package ru.gameservice.entity.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.gameservice.entity.Cost;
import ru.gameservice.enums.CardType;

import java.util.UUID;

/**
 * Карта игры. Хранится в базе данных.
 */

@Data
public class Card {
    private UUID id; // Уникальный идентификатор карты
    private String name; // Название карты
    private CardType type; // Тип карты (существо, здание и т.д.)
    private String cardType; // Тип карты (например, "зелёный", "фиолетовый")
    private Cost cost; // Стоимость карты
    private int points; // Количество очков
    private boolean uniq; // Уникальность карты
    private Placement placement; // Свойства размещения работников
    private String linkedCritterDiscount; // Связанный житель для скидки
    private int maxCount; // Максимальное количество таких карт в игре
    private String extension; // Название дополнения
    @JsonProperty("image_url")
    private String imageUrl; // Ссылка на изображение карты
    private boolean locker;
}

