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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer id;
    @Column(name = "keys")
    private String keys;
    @Column(name = "session_token", length = 2048)
    private String sessionToken;
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "user_id", unique = false, nullable = false, updatable = false)
    private User user;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "time_of_creation")
    private long timeOfCreation;

    public Session(){};
    public Session(String keys, String sessionToken, User user, boolean isActive, long timeOfCreation) {
        this.keys = keys;
        this.sessionToken = sessionToken;
        this.user = user;
        this.isActive = isActive;
        this.timeOfCreation = timeOfCreation;
    }

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

    public static List<String> generateCombinationsPassword(int amountOfCombinations) {
        Set<List<Integer>> combinations = Session.generateCombinations(amountOfCombinations);
        List<String> retorno = new ArrayList<>();

        for(List<Integer> numbers : combinations){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numbers.size(); i += 2) {
                sb.append(numbers.get(i));
                if (i + 1 < numbers.size()) {
                    sb.append(",");
                    sb.append(numbers.get(i + 1));
                    if (i + 2 < numbers.size()) {
                        sb.append(";");
                    }
                }
            }
            retorno.add(sb.toString());
        }
        return retorno;
    }
}
