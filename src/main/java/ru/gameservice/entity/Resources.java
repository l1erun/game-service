package ru.gameservice.entity;
import lombok.*;

import java.io.Serializable;

/**
 * Ресурсы игрока.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Resources implements Serializable{
    private int twigs = 100; // Ветки
    private int resin = 100; // Смола
    private int pebbles = 100; // Камни
    private int berries = 100; // Ягоды

    // Другие необходимые поля
}
