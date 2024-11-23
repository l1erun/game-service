package ru.gameservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gameservice.entity.GameSession;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameSessionRepository extends CrudRepository<GameSession, UUID> {
    Optional<GameSession> findByPin(String pin);
}
