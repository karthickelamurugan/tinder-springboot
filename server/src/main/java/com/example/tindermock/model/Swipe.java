package com.example.tindermock.model;

import jakarta.persistence.*;

@Entity
@Table(name = "swipes")
public class Swipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromUserId;
    private Long toUserId;
    private String direction; // 'like' or 'dislike'
    private Long ts;

    public Swipe() {}
    public Swipe(Long fromUserId, Long toUserId, String direction, Long ts) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.direction = direction;
        this.ts = ts;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFromUserId() { return fromUserId; }
    public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }
    public Long getToUserId() { return toUserId; }
    public void setToUserId(Long toUserId) { this.toUserId = toUserId; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    public Long getTs() { return ts; }
    public void setTs(Long ts) { this.ts = ts; }
}
