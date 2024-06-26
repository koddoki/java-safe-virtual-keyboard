package com.top5nacional.virtualkeyboard.repository;

import com.top5nacional.virtualkeyboard.model.Session;
import com.top5nacional.virtualkeyboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findById(Integer id);
    Optional<Session> findFirstBySessionTokenAndIsActive(String sessionToken, boolean isActive);
    List<Session> findByTimeOfCreationLessThan(long timestamp);
}
