package ru.gameservice.service;

import org.springframework.stereotype.Service;
import ru.gameservice.dto.PlayerDto;
import ru.gameservice.entity.Player;
import ru.gameservice.entity.Resources;
import ru.gameservice.entity.cards.Cost;

@Service
public class PlayerService {

    public Player createNewPlayer(PlayerDto playerDto){
        Player player = new Player();
        player.setPlayerId(playerDto.getId());
        player.setNickname(playerDto.getNickname());

        Resources resources = new Resources();
        player.setResources(resources);
        return player;
    }

    public boolean checkResources(Resources playerResources, Cost costCard) {
        if (playerResources.getBerries() >= costCard.getBerries()
                && playerResources.getResin() >= costCard.getResin()
                && playerResources.getPebbles() >= costCard.getPebbles()
                && playerResources.getTwigs() >= costCard.getTwigs()) {
            playerResources.setBerries(playerResources.getBerries() - costCard.getBerries());
            playerResources.setResin(playerResources.getResin() - costCard.getResin());
            playerResources.setPebbles(playerResources.getPebbles() - costCard.getPebbles());
            playerResources.setTwigs(playerResources.getTwigs() - costCard.getTwigs());
            return true;
        }
        return false;
    }
}
