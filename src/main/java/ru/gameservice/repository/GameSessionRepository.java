package ru.gameservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gameservice.entity.GameSession;

import java.util.UUID;

/**
 * Репозиторий для работы с игровыми сессиями в Redis.
 */
@Repository
public interface GameSessionRepository extends CrudRepository<GameSession, UUID> {
}
