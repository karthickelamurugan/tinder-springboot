package com.example.tindermock.controller;

import com.example.tindermock.model.Match;
import com.example.tindermock.model.Swipe;
import com.example.tindermock.model.User;
import com.example.tindermock.repository.MatchRepository;
import com.example.tindermock.repository.SwipeRepository;
import com.example.tindermock.repository.UserRepository;
import com.example.tindermock.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class SwipeController {
    private final SwipeRepository swipeRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final JwtUtil jwtUtil;

    public SwipeController(SwipeRepository swipeRepository, UserRepository userRepository, MatchRepository matchRepository, JwtUtil jwtUtil) {
        this.swipeRepository = swipeRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.jwtUtil = jwtUtil;
    }

    private Long getUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        Claims c = jwtUtil.parseClaims(token);
        Number userId = (Number) c.get("userId");
        return userId == null ? null : userId.longValue();
    }

    @PostMapping("/{id}/swipe")
    public Object swipe(@PathVariable Long id, @RequestBody Map<String,String> body, @RequestHeader(name = "Authorization", required = false) String auth) {
        Long meId = getUserIdFromToken(auth);
        if (meId == null) return Map.of("error","unauthorized");
        String dir = body.get("direction");
        if (!"like".equals(dir) && !"dislike".equals(dir)) return Map.of("error","direction must be like or dislike");
        if (swipeRepository.existsByFromUserIdAndToUserId(meId, id)) return Map.of("error","already swiped");
        Swipe s = new Swipe(meId, id, dir, System.currentTimeMillis());
        swipeRepository.save(s);
        // check reciprocal like
        boolean reciprocal = !swipeRepository.findByToUserIdAndFromUserIdAndDirection(meId, id, "like").isEmpty();
        if ("like".equals(dir) && reciprocal) {
            // create match
            Match m = new Match(meId + "," + id, System.currentTimeMillis());
            matchRepository.save(m);
            return Map.of("matched", true, "matchId", m.getId());
        }
        return Map.of("matched", false);
    }
}
