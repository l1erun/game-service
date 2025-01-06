package ru.gameservice.entity.locations;

import lombok.Data;

@Data
public class RewardsLocation {
    private int twigs; // Ветки
    private int resin; // Смола
    private int pebbles; // Камни
    private int berries; // Ягоды
    private int cards; // Карты
    private int points; // Очки
    private int any; // Любой ресурс
}
