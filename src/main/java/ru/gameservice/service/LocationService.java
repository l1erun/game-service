package ru.gameservice.service;

import org.springframework.stereotype.Service;
import ru.gameservice.dto.LocationDto;
import ru.gameservice.entity.GameSession;
import ru.gameservice.entity.locations.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class LocationService {

    public List<Location> getAllLocations(GameSession gameSession) {
        List<Location> allLocations = new ArrayList<>();
        if (gameSession.getGameState().getBaseLocations() != null) {
            allLocations.addAll(gameSession.getGameState().getBaseLocations());
        }
        if (gameSession.getGameState().getForestLocations() != null) {
            allLocations.addAll(gameSession.getGameState().getForestLocations());
        }
        if (gameSession.getGameState().getUserLocation() != null) {
            allLocations.addAll(gameSession.getGameState().getUserLocation());
        }
        return allLocations;
    }

    public Optional<Location> findLocationById(List<Location> locations, UUID locationId) {
        return locations.stream()
                .filter(location -> location.getId().equals(locationId))
                .findFirst();
    }

    public boolean hasAvailableWorkerSlot(Location location, UUID playerId) {
        return !location.getOccupiedBy().contains(playerId) &&
                location.getWorkerSlots() > location.getOccupiedBy().size();
    }

    public void allocateWorker(Location location, UUID playerId) {
        location.getOccupiedBy().add(playerId);
    }

    public LocationDto getWorkersSlot(GameSession gameSession, UUID playerId) {
        LocationDto locationDto = new LocationDto();
        for (Location location : gameSession.getGameState().getBaseLocations()) {
            if (hasAvailableWorkerSlot(location, playerId)) {
                locationDto.getBaseLocation().add(location);
            }
        }
        for (Location location : gameSession.getGameState().getForestLocations()) {
            if (hasAvailableWorkerSlot(location, playerId)) {
                locationDto.getForestLocation().add(location);
            }
        }
        for (Location location : gameSession.getGameState().getUserLocation()) {
            if (hasAvailableWorkerSlot(location, playerId)) {
                locationDto.getUserLocation()
                        .computeIfAbsent(location.getId().toString(), key -> new ArrayList<>())
                        .add(location);
            }
        }
        return locationDto;
    }
}