package ru.gameservice.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Локация на игровом поле.
 */
@Getter
@Setter
@NoArgsConstructor
@RedisHash("Location")
public class Location implements Serializable {

    private String name; // Название локации

    private String description; // Описание локации

    private List<UUID> occupiedBy = new ArrayList<>(); // Список идентификаторов игроков, занявших локацию

    // Другие необходимые поля
}
