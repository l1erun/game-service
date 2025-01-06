package ru.gameservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.gameservice.entity.locations.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
public class LocationDto {
    private List<Location> baseLocation = new ArrayList<>();
    private List<Location> forestLocation= new ArrayList<>();
    private Map<String, List<Location>> userLocation = new HashMap<>();
}
