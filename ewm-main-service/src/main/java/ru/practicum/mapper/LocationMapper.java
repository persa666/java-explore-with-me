package ru.practicum.mapper;

import ru.practicum.dto.event.Location;
import ru.practicum.model.Position;

public class LocationMapper {
    public static Location toLocation(Position position) {
        return new Location(
                position.getLat(),
                position.getLon()
        );
    }

    public static Position toPosition(Location location) {
        return new Position(
                location.getLat(),
                location.getLon()
        );
    }
}
