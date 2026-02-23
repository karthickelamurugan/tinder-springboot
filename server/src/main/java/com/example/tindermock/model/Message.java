package com.example.tindermock.model;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long matchId;
    private Long fromUserId;
    private String text;
    private Long ts;

    public Message() {}
    public Message(Long matchId, Long fromUserId, String text, Long ts) {
        this.matchId = matchId; this.fromUserId = fromUserId; this.text = text; this.ts = ts;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }
    public Long getFromUserId() { return fromUserId; }
    public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Long getTs() { return ts; }
    public void setTs(Long ts) { this.ts = ts; }
}
