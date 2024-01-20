package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.State;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    @Query
    Page<Event> findByInitiatorId(Long userId, Pageable pageable);

    @Query
    Page<Event> findByInitiatorIdInAndEventDateAfterAndState(Collection<Long> userId, LocalDateTime eventDate,
                                                             State state, Pageable pageable);
}
