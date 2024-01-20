package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ParticipationRequest;

import java.util.Collection;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("SELECT COUNT(r) FROM ParticipationRequest AS r WHERE r.event.id = :eventId AND r.status = 'CONFIRMED'")
    Integer findCountByEventId(@Param("eventId") Long eventId);

    @Query
    List<ParticipationRequest> findByEventIdIn(Collection<Long> eventIds);

    @Query
    List<ParticipationRequest> findByRequesterId(Long userId);

    @Query
    List<ParticipationRequest> findByEventId(Long eventId);

    @Query("SELECT COUNT(r) FROM ParticipationRequest AS r WHERE r.event.id IN :eventId AND r.status = 'CONFIRMED' " +
            "GROUP BY r.id")
    List<Integer> findCountByEventIdIn(@Param("eventId") Collection<Long> eventIds);
}
