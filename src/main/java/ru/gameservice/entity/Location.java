package ru.gameservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Локация на игровом поле.
 */
@Data
public class Location implements Serializable {

    private String name; // Название локации

    private String description; // Описание локации

    private List<UUID> occupiedBy; // Список идентификаторов игроков, занявших локацию

    // Другие необходимые поля
}
