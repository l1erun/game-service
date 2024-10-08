package ru.gameservice.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Стоимость карты.
 */
@Data
public class Cost implements Serializable {

    private int twigs; // Ветки

    private int resin; // Смола

    private int pebbles; // Камни

    private int berries; // Ягоды

    // добавить ресурсы
}
