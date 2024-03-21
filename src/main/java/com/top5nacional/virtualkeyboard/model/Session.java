package com.top5nacional.virtualkeyboard.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="sessions")
public class Session {
    @Id
    @Column(name = "session_id")
    private String id;
    @Column(name = "keys")
    private String keys;
    @Column(name = "session_token")
    private String sessionToken;
    @OneToOne(optional=false)
    @JoinColumn(
            name="user_id", unique=true, nullable=false, updatable=false)
    private User user;
    @Column(name = "is_active")
    private boolean isActive;
}
