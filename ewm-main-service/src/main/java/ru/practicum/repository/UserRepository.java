package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query
    Page<User> findByIdIn(Long[] ids, Pageable pageable);
}
