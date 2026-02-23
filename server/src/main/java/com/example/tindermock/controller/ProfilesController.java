package com.example.tindermock.controller;

import com.example.tindermock.model.User;
import com.example.tindermock.repository.SwipeRepository;
import com.example.tindermock.repository.UserRepository;
import io.jsonwebtoken.Claims;
import com.example.tindermock.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profiles")
public class ProfilesController {
    private final UserRepository userRepository;
    private final SwipeRepository swipeRepository;
    private final JwtUtil jwtUtil;

    public ProfilesController(UserRepository userRepository, SwipeRepository swipeRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.swipeRepository = swipeRepository;
        this.jwtUtil = jwtUtil;
    }

    // Very simple auth: expect Authorization: Bearer <token>
    private Long getUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        Claims c = jwtUtil.parseClaims(token);
        Number userId = (Number) c.get("userId");
        return userId == null ? null : userId.longValue();
    }

    @GetMapping
    public Map<String,Object> listProfiles(@RequestHeader(name = "Authorization", required = false) String auth,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int limit) {
        Long meId = getUserIdFromToken(auth);
        if (limit > 50) limit = 50;
        List<User> all = userRepository.findAll();
        List<User> filtered = all.stream()
                .filter(u -> !u.getId().equals(meId))
                .limit(limit)
                .collect(Collectors.toList());
        List<Map<String,Object>> results = filtered.stream().map(u -> Map.of(
                "id", u.getId(),
                "name", u.getName(),
                "age", u.getAge(),
                "bio", u.getBio(),
                "photos", u.getPhotos() == null ? new String[0] : u.getPhotos().split(",")
        )).collect(Collectors.toList());
        return Map.of("page", page, "limit", limit, "results", results, "total", results.size());
    }

    @GetMapping("/{id}")
    public Object getProfile(@PathVariable Long id) {
        return userRepository.findById(id).map(u -> Map.of(
                "id", u.getId(),
                "name", u.getName(),
                "age", u.getAge(),
                "bio", u.getBio(),
                "photos", u.getPhotos() == null ? new String[0] : u.getPhotos().split(",")
        )).orElse(Map.of("error","not found"));
    }
}
