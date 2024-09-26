package ru.gameservice.entity;
import lombok.Data;

import java.io.Serializable;

/**
 * Ресурсы игрока.
 */
@Data
public class Resources implements Serializable{

    private int twigs; // Ветки

    private int resin; // Смола

    private int pebbles; // Камни

    private int berries; // Ягоды

    // Другие необходимые поля
}
