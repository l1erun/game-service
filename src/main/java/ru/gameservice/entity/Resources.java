package ru.gameservice.entity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Ресурсы игрока.
 */
@Getter
@Setter
@NoArgsConstructor
public class Resources implements Serializable{

    private int twigs; // Ветки

    private int resin; // Смола

    private int pebbles; // Камни

    private int berries; // Ягоды

    // Другие необходимые поля
}
