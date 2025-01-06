package ru.gameservice.service;

import org.springframework.stereotype.Service;
import ru.gameservice.entity.cards.Card;
import ru.gameservice.entity.locations.Location;
import ru.gameservice.enums.CardType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class CardService {

    public Card findCardIdOnCardsList(List<Card> cardList, UUID cardId) {
        return cardList.stream()
                .filter(card -> card.getId().equals(cardId))
                .findFirst()
                .orElse(null);
//                .orElseThrow(() -> new NoSuchElementException("Карта с id: "
//                        + cardId + " не найдена в списке " + cardList));
    }

    public boolean checkFreeBuildCardInCardPlayer(Card card, Card playerCard) {
        if (card.getType().equals(CardType.CREATURE)
                && !card.isUniq()
                && playerCard.getLinkedCritterDiscount().contains(card.getName())
                && !card.isLocker()) {
            return true;
        }
        return false;
    }

//    public Location createUserLocation(Card card){
//        Location userLocation = new Location();
//    }
}
