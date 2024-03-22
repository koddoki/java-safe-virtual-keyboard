package com.top5nacional.virtualkeyboard.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Data
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @Column(name = "session_id")
    private String id;
    @Column(name = "keys")
    private String keys;
    @Column(name = "session_token")
    private String sessionToken;
    @OneToOne(optional = false)
    @JoinColumn(
            name = "user_id", unique = true, nullable = false, updatable = false)
    private User user;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "time_of_creation")
    private long timeOfCreation;
    
    public static Set<List<Integer>> generateCombinations(int amountOfCombinations) {
        Set<List<Integer>> combinations = new HashSet<>();
        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }

        while (combinations.size() < amountOfCombinations) {
            Collections.shuffle(numbers, new Random());
            combinations.add(new ArrayList<>(numbers));
        }

        return combinations;
    }
}
