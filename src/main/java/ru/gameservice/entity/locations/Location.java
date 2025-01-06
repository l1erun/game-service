package ru.gameservice.entity.locations;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Локация на игровом поле.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@RedisHash("Location")
public class Location implements Serializable {
    private List<UUID> occupiedBy = new ArrayList<>(); // Список идентификаторов игроков, занявших локацию
    private UUID id;
    private String name;
    private String type;
    private String extension; // Название дополнения
    private RewardsLocation rewards; // Награды за использование локации
    private CostLocation cost;
    private String description;
    private String imageUrl;
    private boolean uniq;
    private int workerSlots;
    // Другие необходимые поля
}
