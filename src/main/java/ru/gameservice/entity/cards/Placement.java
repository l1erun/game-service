package ru.gameservice.entity.cards;

import lombok.Data;

@Data
public class Placement {
    private int capacity; // Количество работников, которых можно разместить
    private String allowedWorkers; // Кто может размещать работников ("any", "user" или null)
}

