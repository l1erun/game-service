package ru.gameservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * Событие на игровом поле.
 */
@Data
public class Event implements Serializable {

    private String name; // Название события

    private String description; // Описание события

    private boolean isCompleted; // Завершено ли событие

    private UUID completedBy; // Идентификатор игрока, завершившего событие

    // Другие необходимые поля
}
