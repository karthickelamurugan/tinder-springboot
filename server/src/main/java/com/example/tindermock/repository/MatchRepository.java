package com.example.tindermock.repository;

import com.example.tindermock.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByUserIdsContaining(String userIdPart);
}
