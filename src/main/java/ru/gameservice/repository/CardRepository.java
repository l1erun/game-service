package ru.gameservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.gameservice.entity.Card;

import java.util.UUID;

public interface CardRepository extends CrudRepository<Card, UUID> {
}
