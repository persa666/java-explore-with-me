package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.hit.Hit;
import ru.practicum.hit.HitShort;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Integer> {

    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(h.ip) AS hits FROM Hit AS h WHERE h.uri IN (:uris)" +
            " AND h.timestamp BETWEEN :start AND :end GROUP BY h.app, h.uri ORDER BY hits DESC")
    List<HitShort> findByTimestampBetweenAndUriIn(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                                  @Param("uris") String[] uris);

    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(DISTINCT h.ip) AS hits FROM Hit AS h WHERE h.uri IN (:uris)" +
            " AND h.timestamp BETWEEN :start AND :end GROUP BY h.app, h.uri ORDER BY hits DESC")
    List<HitShort> findByTimestampBetweenAndUriInDistinct(@Param("start") LocalDateTime start,
                                                          @Param("end") LocalDateTime end,
                                                          @Param("uris") String[] uris);

    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(h.ip) AS hits FROM Hit AS h WHERE h.timestamp BETWEEN :start " +
            "AND :end GROUP BY h.app, h.uri ORDER BY hits DESC")
    List<HitShort> findByTimestampBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(Distinct h.ip) AS hits FROM Hit AS h WHERE h.timestamp " +
            "BETWEEN :start AND :end GROUP BY h.app, h.uri ORDER BY hits DESC")
    List<HitShort> findByTimestampBetweenDistinct(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
