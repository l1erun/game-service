package ru.gameservice.entity;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Состояние игрового поля. Живет в течение сессии.
 */
@Data
@RedisHash("BoardState")
public class BoardState implements Serializable {
    private List<Location> locations = new ArrayList<>(); // Доступные локации на игровом поле

    private List<Event> events = new ArrayList<>(); // Доступные события

    // Другие необходимые поля
}
