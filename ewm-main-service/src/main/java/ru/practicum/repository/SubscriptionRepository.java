package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Subscription;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query
    Page<Subscription> findByAuthorOfContentId(Long userId, Pageable pageable);

    @Query
    @Transactional
    void deleteByAuthorOfContentId(Long userId);

    @Query
    @Transactional
    void deleteBySubscriberId(Long userId);

    @Query
    Page<Subscription> findBySubscriberId(Long userId, Pageable pageable);

    @Query("SELECT s.authorOfContent.id FROM Subscription AS s WHERE s.subscriber.id = :userId")
    List<Long> findAuthorOfContentIdBySubscriberId(@Param("userId") Long userId);
}
