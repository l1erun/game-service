package ru.gameservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class PlayerActionDto {
    private UUID playerId;
    private String actionType;
    private Object actionData;
}
