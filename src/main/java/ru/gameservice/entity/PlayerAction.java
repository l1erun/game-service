package ru.gameservice.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayerAction {
    private UUID playerId;
    private String actionType;
    private Object actionData;
}
