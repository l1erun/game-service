package ru.gameservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.gameservice.entity.Player;
import ru.gameservice.entity.Resources;
import ru.gameservice.entity.cards.Card;
import ru.gameservice.enums.Season;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class PlayerAreaDto {
    private UUID playerId; // Идентификатор игрока
    private String nickname; // Отображаемое имя игрока
    private String avatarUrl;
    private Boolean flagActivePlayer;
    private List<Card> hand ; // Карты на руках
    private List<Card> city ; // Построенные карты в городе
    private Resources resources; // Ресурсы игрока
    private int points = 0; // Текущее количество очков
    private int workers;
    private Season currentSeason; // Текущий сезон игры
    private List<Card> meadowCardsList = new ArrayList<>();

    public void toPlayerAreaDto(Player player) {
        setPlayerId(player.getPlayerId());
        setNickname(player.getNickname());
        setAvatarUrl(player.getAvatarUrl());
        setFlagActivePlayer(player.getFlagActivePlayer());
        setHand(player.getHand());
        setCity(player.getCity());
        setResources(player.getResources());
        setPoints(player.getPoints());
        setWorkers(player.getWorkers());
        setCurrentSeason(player.getCurrentSeason());
    }
}
