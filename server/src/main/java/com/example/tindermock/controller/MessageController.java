package com.example.tindermock.controller;

import com.example.tindermock.model.Message;
import com.example.tindermock.model.Match;
import com.example.tindermock.repository.MessageRepository;
import com.example.tindermock.repository.MatchRepository;
import com.example.tindermock.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;
    private final MatchRepository matchRepository;
    private final JwtUtil jwtUtil;

    public MessageController(MessageRepository messageRepository, MatchRepository matchRepository, JwtUtil jwtUtil) {
        this.messageRepository = messageRepository;
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

    @GetMapping("/{matchId}")
    public Object listMessages(@PathVariable Long matchId, @RequestHeader(name = "Authorization", required = false) String auth) {
        Long meId = getUserIdFromToken(auth);
        if (meId == null) return Map.of("error","unauthorized");
        Match m = matchRepository.findById(matchId).orElse(null);
        if (m == null) return Map.of("error","match not found");
        if (!m.getUserIds().contains(meId.toString())) return Map.of("error","not authorized");
        List<Message> msgs = messageRepository.findByMatchId(matchId);
        return msgs;
    }

    @PostMapping("/{matchId}")
    public Object postMessage(@PathVariable Long matchId, @RequestBody Map<String,String> body, @RequestHeader(name = "Authorization", required = false) String auth) {
        Long meId = getUserIdFromToken(auth);
        if (meId == null) return Map.of("error","unauthorized");
        Match m = matchRepository.findById(matchId).orElse(null);
        if (m == null) return Map.of("error","match not found");
        if (!m.getUserIds().contains(meId.toString())) return Map.of("error","not authorized");
        String text = body.get("text");
        if (text == null || text.isBlank()) return Map.of("error","text required");
        Message msg = new Message(matchId, meId, text, System.currentTimeMillis());
        messageRepository.save(msg);
        return msg;
    }
}
