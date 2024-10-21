package ru.gameservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@ToString
public class GameSessionRequest {
    @NotNull
    private UUID id;

    @NotNull
    private Set<UUID> players; // Список игроков в сессии
}
