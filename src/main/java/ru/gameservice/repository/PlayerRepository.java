package ru.gameservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gameservice.entity.Player;

import java.util.UUID;

@Repository
public interface PlayerRepository extends CrudRepository<Player, UUID> {
}
