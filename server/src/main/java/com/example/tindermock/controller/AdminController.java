package com.example.tindermock.controller;

import com.example.tindermock.model.User;
import com.example.tindermock.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {
    private final UserRepository userRepository;
    public AdminController(UserRepository userRepository) { this.userRepository = userRepository; }

    @PostMapping("/api/admin/seed")
    public Object seed() {
        userRepository.deleteAll();
        List<User> seed = List.of(
                new User("alice", "$2a$10$devhash", "Alice", 26, "Loves hikes and coffee", "https://via.placeholder.com/200"),
                new User("bob", "$2a$10$devhash", "Bob", 29, "Guitarist and foodie", "https://via.placeholder.com/200"),
                new User("carla", "$2a$10$devhash", "Carla", 24, "Photographer", "https://via.placeholder.com/200"),
                new User("dan", "$2a$10$devhash", "Dan", 31, "Tech enthusiast", "https://via.placeholder.com/200"),
                new User("eva", "$2a$10$devhash", "Eva", 27, "Runner & traveler", "https://via.placeholder.com/200")
        );
        userRepository.saveAll(seed);
        return List.of("ok");
    }
}
