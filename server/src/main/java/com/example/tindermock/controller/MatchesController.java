package com.example.tindermock.controller;

import com.example.tindermock.model.Match;
import com.example.tindermock.model.User;
import com.example.tindermock.repository.MatchRepository;
import com.example.tindermock.repository.UserRepository;
import com.example.tindermock.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
public class MatchesController {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public MatchesController(MatchRepository matchRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    private Long getUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        Claims c = jwtUtil.parseClaims(token);
        Number userId = (Number) c.get("userId");
        return userId == null ? null : userId.longValue();
    }

    @GetMapping
    public Object listMatches(@RequestHeader(name = "Authorization", required = false) String auth) {
        Long meId = getUserIdFromToken(auth);
        if (meId == null) return Map.of("error","unauthorized");
        List<Match> all = matchRepository.findByUserIdsContaining(meId.toString());
        var out = all.stream().map(m -> {
            String[] parts = m.getUserIds().split(",");
            Long otherId = parts[0].equals(meId.toString()) ? Long.parseLong(parts[1]) : Long.parseLong(parts[0]);
            User u = userRepository.findById(otherId).orElse(null);
            return Map.of("id", m.getId(), "user", Map.of("id", u.getId(), "name", u.getName(), "age", u.getAge(), "photos", u.getPhotos()));
        }).collect(Collectors.toList());
        return out;
    }
}
