package ru.gameservice.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class PlayerDto {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("avatarUrl")
    private String avatarUrl;
    @JsonProperty("language")
    private String language;
}
