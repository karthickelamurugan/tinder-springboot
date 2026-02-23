package com.example.tindermock.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // store as comma separated user ids (simple)
    private String userIds; // e.g. "1,2"
    private Long created;

    public Match() {}
    public Match(String userIds, Long created) {
        this.userIds = userIds; this.created = created;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserIds() { return userIds; }
    public void setUserIds(String userIds) { this.userIds = userIds; }
    public Long getCreated() { return created; }
    public void setCreated(Long created) { this.created = created; }
}
