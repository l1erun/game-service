package ru.gameservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Состояние игрового поля. Живет в течение сессии.
 */
@Data
public class BoardState implements Serializable {
    private List<Location> locations; // Доступные локации на игровом поле

    private List<Event> events; // Доступные события

    // Другие необходимые поля
}
