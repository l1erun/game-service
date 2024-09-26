package ru.gameservice.entity;
import lombok.Data;
import ru.gameservice.enums.CardType;

import java.io.Serializable;
import java.util.UUID;

/**
 * Карта в игре. Информация получена из Card Service.
 */
@Data
public class Card implements Serializable{

    private UUID id; // Уникальный идентификатор карты

    private String name; // Название карты

    private CardType type; // Тип карты (существо, здание и т.д.)

    private String subtype; // Подтип карты (если есть)

    private Cost cost; // Стоимость карты

    private int points; // Количество очков

    private String expansion; // Дополнение, к которому относится карта

    private String description; // Описание эффекта карты

    private String imageUrl; // Ссылка на изображение карты

    // Другие необходимые поля
}