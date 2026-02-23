package com.example.tindermock.controller;

import com.example.tindermock.model.User;
import com.example.tindermock.repository.UserRepository;
import com.example.tindermock.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        if (username == null || password == null) return ResponseEntity.badRequest().body(Map.of("error","username and password required"));
        if (userRepository.existsByUsername(username)) return ResponseEntity.status(409).body(Map.of("error","username taken"));
        String name = (String) body.getOrDefault("name", username);
        Integer age = body.containsKey("age") ? (Integer) body.get("age") : null;
        String bio = (String) body.getOrDefault("bio", "");
        String photos = body.containsKey("photos") ? String.join(",", (java.util.List<String>) body.get("photos")) : "";
        String pwHash = passwordEncoder.encode(password);
        User u = new User(username, pwHash, name, age, bio, photos);
        userRepository.save(u);
        String token = jwtUtil.generateToken(u.getUsername(), u.getId());
        return ResponseEntity.status(201).body(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        if (username == null || password == null) return ResponseEntity.badRequest().body(Map.of("error","username and password required"));
        return userRepository.findByUsername(username)
                .map(u -> {
                    if (!passwordEncoder.matches(password, u.getPasswordHash())) return ResponseEntity.status(401).body(Map.of("error","invalid credentials"));
                    String token = jwtUtil.generateToken(u.getUsername(), u.getId());
                    return ResponseEntity.ok(Map.of("token", token));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error","invalid credentials")));
    }
}
