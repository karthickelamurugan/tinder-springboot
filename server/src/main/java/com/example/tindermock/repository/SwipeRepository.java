package com.example.tindermock.repository;

import com.example.tindermock.model.Swipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwipeRepository extends JpaRepository<Swipe, Long> {
    List<Swipe> findByFromUserId(Long fromUserId);
    List<Swipe> findByToUserIdAndFromUserIdAndDirection(Long toUserId, Long fromUserId, String direction);
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
