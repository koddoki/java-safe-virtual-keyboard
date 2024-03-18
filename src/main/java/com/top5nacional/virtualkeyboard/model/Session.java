package com.top5nacional.virtualkeyboard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="sessions")
public class Session {
    @Id
    private String id;
    private String keys;
    private boolean isActive;
}
