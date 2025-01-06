package ru.gameservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaticService {

    /**
     * Метод для получения карт и удаления их из исходного списка
     * @param cardList
     * @param count
     * @return
     * @param <T>
     */
    public static <T> List<T> drawCards(List<T> cardList, int count) {
        if (count > cardList.size()) {
            throw new IllegalArgumentException("Недостаточно карт в списке для выбора " + count + " карт.");
        }
        Collections.shuffle(cardList);
        List<T> selectedCards = new ArrayList<>(cardList.subList(0, count)); // Получаем первые count карт
        cardList.subList(0, count).clear(); // Удаляем эти карты из исходного списка
        return selectedCards;
    }
}
