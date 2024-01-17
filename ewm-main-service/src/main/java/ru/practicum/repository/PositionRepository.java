package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    @Query
    Position findByLatAndLon(float lat, float lon);
}
