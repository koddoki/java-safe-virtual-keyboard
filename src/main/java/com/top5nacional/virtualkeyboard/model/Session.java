package com.top5nacional.virtualkeyboard.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer id;
    @Column(name = "keys")
    private String keys;
    @Column(name = "session_token", length = 2048)
    private String sessionToken;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "time_of_creation")
    private long timeOfCreation;

    public Session(){};
    public Session(String keys, String sessionToken) {
        this.keys = keys;
        this.sessionToken = sessionToken;
        this.isActive = true;
        this.timeOfCreation = System.currentTimeMillis();
    }

    private static List<Integer> generateRandomNumberSequence() {
        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers, new Random());
        return new ArrayList<>(numbers);
    }

    public static String generateRandomKey() {
        List<Integer> combinations = Session.generateRandomNumberSequence();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < combinations.size(); i += 2) {
            sb.append(combinations.get(i));
            if (i + 1 < combinations.size()) {
                sb.append(",");
                sb.append(combinations.get(i + 1));
                if (i + 2 < combinations.size()) {
                    sb.append(";");
                }
            }
        }
        return sb.toString();
    }

    public static List<List<Integer>> convertKeysToList(String stringKeys) {
        return Arrays.stream(stringKeys.split(";"))
                .map(innerKey -> Arrays.stream(innerKey.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
