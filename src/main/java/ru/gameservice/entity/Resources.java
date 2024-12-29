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
    private int twigs = 1; // Ветки
    private int resin = 1; // Смола
    private int pebbles = 1; // Камни
    private int berries = 1; // Ягоды

    // Другие необходимые поля
}
